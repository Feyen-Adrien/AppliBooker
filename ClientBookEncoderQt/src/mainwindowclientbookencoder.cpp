#include "mainwindowclientbookencoder.h"
#include "ui_mainwindowclientbookencoder.h"
#include "unistd.h"
#include <QInputDialog>
#include <QMessageBox>
#include <iostream>
#include "TCP.h"
#include "OBEP.h"
using namespace std;

int sClient;
int NbLivre;
char IpServeur[50] = "192.168.21.130";

bool OBEP_Login_Client(const char* user, const char* password);
bool OBEP_ADD_AUTHOR_Client(const char* nom, const char* prenom, const char* date);
bool OBEP_ADD_SUBJECT_Client(const char *nom);
bool OBEP_ADD_BOOK_Client(const char* titre, const char* isbn, int page ,float prix,int annee,int stock,const char* auteur,const char* sujet);
void OBEP_Logout();
bool isValidDate(const char* date);
bool verifier_isbn(const char *isbn);
void Echange(char* requete,char* reponse);


MainWindowClientBookEncoder::MainWindowClientBookEncoder(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindowClientBookEncoder)
{
    ui->setupUi(this);
    ::close(2);

    //this->setFixedSize(1068, 301);

    // Configuration de la table des employes (Personnel Garage)
    ui->tableWidgetEncodedBooks->setColumnCount(9);
    ui->tableWidgetEncodedBooks->setRowCount(0);
    QStringList labelsTableEmployes;
    labelsTableEmployes << "Id" << "Titre" << "Auteur" << "Sujet" << "ISBN" << "Pages" << "Année" << "Prix" << "Stock";
    ui->tableWidgetEncodedBooks->setHorizontalHeaderLabels(labelsTableEmployes);
    ui->tableWidgetEncodedBooks->horizontalHeader()->setVisible(true);
    ui->tableWidgetEncodedBooks->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetEncodedBooks->verticalHeader()->setVisible(false);
    ui->tableWidgetEncodedBooks->horizontalHeader()->setStyleSheet("background-color: lightyellow");
    int columnWidths[] = {35, 250, 200, 200, 150, 50, 50, 50, 40};
    for (int col = 0; col < 9; ++col)
        ui->tableWidgetEncodedBooks->setColumnWidth(col, columnWidths[col]);

    this->logoutOk();

    // Connexion sur le serveur
    if((sClient = ClientSocket(IpServeur,50000))==-1)
    {
        perror("Erreur de ClientSocket");
        exit(1);
    }
    printf("Client connecté sur le serveur.\n");
}

MainWindowClientBookEncoder::~MainWindowClientBookEncoder() {
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table des livres encodés (ne pas modifier) ////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::addTupleTableBooks(int id,
                                                     string title,
                                                     string author,
                                                     string subject,
                                                     string isbn,
                                                     int pageCount,
                                                     int publishYear,
                                                     float price,
                                                     int stockQuantity)
{
    int nb = ui->tableWidgetEncodedBooks->rowCount();
    nb++;
    ui->tableWidgetEncodedBooks->setRowCount(nb);
    ui->tableWidgetEncodedBooks->setRowHeight(nb-1,10);

    // id
    QTableWidgetItem *item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(id));
    ui->tableWidgetEncodedBooks->setItem(nb-1,0,item);

    // title
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setText(QString::fromStdString(title));
    ui->tableWidgetEncodedBooks->setItem(nb-1,1,item);

    // author
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(author));
    ui->tableWidgetEncodedBooks->setItem(nb-1,2,item);

    // subject
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(subject));
    ui->tableWidgetEncodedBooks->setItem(nb-1,3,item);

    // isbn
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(isbn));
    ui->tableWidgetEncodedBooks->setItem(nb-1,4,item);

    // pageCount
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(pageCount));
    ui->tableWidgetEncodedBooks->setItem(nb-1,5,item);

    // publishYear
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(publishYear));
    ui->tableWidgetEncodedBooks->setItem(nb-1,6,item);

    // price
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(price));
    ui->tableWidgetEncodedBooks->setItem(nb-1,7,item);

    // stockQuantity
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(stockQuantity));
    ui->tableWidgetEncodedBooks->setItem(nb-1,8,item);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::clearTableBooks() {
    ui->tableWidgetEncodedBooks->setRowCount(0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles des comboboxes (ne pas modifier) //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::addComboBoxAuthors(string author){
    ui->comboBoxAuthors->addItem(QString::fromStdString(author));
}

string MainWindowClientBookEncoder::getSelectionAuthor() const {
    return ui->comboBoxAuthors->currentText().toStdString();
}

void MainWindowClientBookEncoder::clearComboBoxAuthors() {
    ui->comboBoxAuthors->clear();
}

void MainWindowClientBookEncoder::addComboBoxSubjects(string subject){
    ui->comboBoxSubjects->addItem(QString::fromStdString(subject));
}

string MainWindowClientBookEncoder::getSelectionSubject() const {
    return ui->comboBoxSubjects->currentText().toStdString();
}

void MainWindowClientBookEncoder::clearComboBoxSubjects() {
    ui->comboBoxSubjects->clear();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonction utiles de la fenêtre (ne pas modifier) ////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientBookEncoder::getTitle() const {
    return ui->lineEditTitle->text().toStdString();
}

string MainWindowClientBookEncoder::getIsbn() const {
    return ui->lineEditIsbn->text().toStdString();
}

int MainWindowClientBookEncoder::getPageCount() const {
    return ui->spinBoxPageCount->value();
}

float MainWindowClientBookEncoder::getPrice() const {
    return ui->doubleSpinBoxPrice->value();
}

int MainWindowClientBookEncoder::getPublishYear() const {
    return ui->spinBoxPublishYear->value();
}

int MainWindowClientBookEncoder::getStockQuantity() const {
    return ui->spinBoxStockQuantity->value();
}

void MainWindowClientBookEncoder::loginOk() {
    ui->pushButtonClear->setEnabled(true);
    ui->pushButtonAddBook->setEnabled(true);
    ui->pushButtonAddAuthor->setEnabled(true);
    ui->pushButtonAddSubject->setEnabled(true);
    ui->actionLogin->setEnabled(false);
    ui->actionLogout->setEnabled(true);
}

void MainWindowClientBookEncoder::logoutOk() {
    ui->pushButtonClear->setEnabled(false);
    ui->pushButtonAddBook->setEnabled(false);
    ui->pushButtonAddAuthor->setEnabled(false);
    ui->pushButtonAddSubject->setEnabled(false);
    ui->actionLogin->setEnabled(true);
    ui->actionLogout->setEnabled(false);
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier) ///////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::dialogMessage(const string& title,const string& message) {
   QMessageBox::information(this,QString::fromStdString(title),QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::dialogError(const string& title,const string& message) {
   QMessageBox::critical(this,QString::fromStdString(title),QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientBookEncoder::dialogInputText(const string& title,const string& question) {
    return QInputDialog::getText(this,QString::fromStdString(title),QString::fromStdString(question)).toStdString();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int MainWindowClientBookEncoder::dialogInputInt(const string& title,const string& question) {
    return QInputDialog::getInt(this,QString::fromStdString(title),QString::fromStdString(question));
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions gestion des boutons et items de menu (TO DO) /////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::on_pushButtonAddAuthor_clicked() {
    string lastName = this->dialogInputText("Nouvel auteur","Nom ?");
    if(strlen(lastName.c_str())==0)
    {
        this->dialogError("Nouvel auteur","Veuillez entrer un nom pour l'auteur !");
    }
    else
    {
        string firstName = this->dialogInputText("Nouvel auteur","Prénom ?");   
        if(strlen(firstName.c_str())==0)
        {
            this->dialogError("Nouvel auteur","Veuillez entrer un prénom pour l'auteur !");
        }
        else
        {
            string birthDate = this->dialogInputText("Nouvel auteur","Date de naissance (yyyy-mm-dd) ?");
            if(!isValidDate(birthDate.c_str()))
            {
                this->dialogError("Nouvel auteur","Veuillez entrer une date valide !");
            }
            else
            {
                if(!OBEP_ADD_AUTHOR_Client(lastName.c_str(),firstName.c_str(),birthDate.c_str()))
                {
                    this->dialogError("Nouvel auteur","Erreur lors de l'insertion !");
                }
                else
                {
                    if(!OBEP_Operation(1)) // MAJ AJOUT
                    {
                        this->dialogError("Nouvel auteur","Erreur lecture des auteurs !");   
                    }
                }
            }
        }
    }
}

void MainWindowClientBookEncoder::on_pushButtonAddSubject_clicked() {
    string name = this->dialogInputText("Nouveau sujet","Nom ?");
    if(strlen(name.c_str())==0)
    {
        this->dialogError("Nouveau sujet","Veuillez entrer un nom pour le sujet !");
    }
    else
    {
        if(!OBEP_ADD_SUBJECT_Client(name.c_str()))
        {
            this->dialogError("Nouveau sujet","Erreur lors de l'insertion !");
        }
        else
        {
            if(!OBEP_Operation(2))
            {
                this->dialogError("Nouveau sujet","Erreur lecture des sujets !");
            }
        }
    }
    cout << "Nom : " << name << endl;
}

void MainWindowClientBookEncoder::on_pushButtonAddBook_clicked() {
    if(strlen(this->getTitle().c_str())==0)
    {
        this->dialogError("Ajouter livre","Veuillez entrer un titre !");
    }
    else
    {
        if (verifier_isbn(this->getIsbn().c_str()) == false)
        {
            this->dialogError("Ajouter livre","Format incorrect pour ISBN !");
        }
        else
        {
            if(this->getPageCount()<1)
            {
                this->dialogError("Ajouter livre","Le nombre de page du livre doit être >0 !");
            }
            else
            {
                if(this->getPrice() <= 0.0)
                {
                    this->dialogError("Ajouter livre","Le prix doit être sup ou égal à 0 !");
                }
                else
                {
                    if (this->getStockQuantity() <0)
                    {
                        this->dialogError("Ajouter livre","La quantité doit être supérieur à 0");
                    }
                    else
                    {
                        if(!OBEP_ADD_BOOK_Client(this->getTitle().c_str(),this->getIsbn().c_str(),this->getPageCount(),this->getPrice(),this->getPublishYear(),this->getStockQuantity(),this->getSelectionAuthor().c_str(),this->getSelectionSubject().c_str()))
                        {
                            this->dialogError("Ajouter livre","Erreur lors de l'insertion !");
                        }
                        else
                        {
                            /*if(!OBEP_Operation(3))
                            {
                                this->dialogError("Ajouter livre","Erreur lecture des livres !");
                            }*/

                        addTupleTableBooks(NbLivre,this->getTitle(),this->getSelectionAuthor(),this->getSelectionSubject(),this->getIsbn(),this->getPageCount(),this->getPublishYear(),this->getPrice(),this->getStockQuantity());
                        }
                    }
                }
            }
        }
    }

    cout << "title = " << this->getTitle() << endl;
    cout << "Isbn = " << this->getIsbn() << endl;
    cout << "PageCount = " << this->getPageCount() << endl;
    cout << "Price = " << this->getPrice() << endl;
    cout << "PublishYear = " << this->getPublishYear() << endl;
    cout << "Stock = " << this->getStockQuantity() << endl;

    cout << "selection auteur = " << this->getSelectionAuthor() << endl;
    cout << "selection sujet  = " << this->getSelectionSubject() << endl;
}

void MainWindowClientBookEncoder::on_pushButtonClear_clicked() {
    ui->lineEditTitle->clear();
    ui->lineEditIsbn->clear();
    ui->spinBoxPageCount->setValue(0);
    ui->doubleSpinBoxPrice->setValue(0);
    ui->spinBoxPublishYear->setValue(0);
    ui->spinBoxStockQuantity->setValue(0);
}

void MainWindowClientBookEncoder::on_actionLogin_triggered() {
    
    // vérification qu'un LOGIN est bien encodé
    string login = this->dialogInputText("Entrée en session","Login ?"); //le caractère ' ' fait bugger
    if(strlen(login.c_str()) == 0)
    {
        this->dialogError("LOGIN","Veuillez entrer un identifiant !");
    }
    else
    {
        // vérification qu'un mdp est bien encodé
        string password = this->dialogInputText("Entrée en session","Password ?");

        if(strlen(password.c_str()) == 0)
        {
            this->dialogError("LOGIN","Veuillez entrer un mot de passe !");
        }
        else
        {
            //envoyer au serveur la commande + vérif
            if(OBEP_Login_Client(login.c_str(),password.c_str())==false)
            {
                this->dialogError("LOGIN","Mauvais identifiants !");
            }
            else
            {
                this->loginOk();
                if(!OBEP_Operation(1))
                {
                    this->dialogError("LOGIN","Erreur lecture des auteurs !");
                }
                if(!OBEP_Operation(2))
                {
                    this->dialogError("LOGIN","Erreur lecture des sujets !");
                }
                /*if(!OBEP_Operation(3)) //pas appeler car il doit seulement voir les livres qu'il encode lui
                {
                    this->dialogError("LOGIN","Erreur lecture des livres !");
                }*/
            }

        }

    }
    
}

void MainWindowClientBookEncoder::on_actionLogout_triggered() {
    // envoyer au serveur la commande
    OBEP_Logout();
    this->logoutOk();
    clearComboBoxAuthors();
    clearComboBoxSubjects();
    clearTableBooks();
    
    
}

void MainWindowClientBookEncoder::on_actionQuitter_triggered(){
    QApplication::exit(0);
}




// gestion du protocole OBEP

bool OBEP_Login_Client(const char* user,const char* password)
{
    char requete[200],reponse[200];
    bool onContinue = true;

    // Construction de la requête 
    sprintf(requete,"LOGIN#%s#%s",user,password);

    // Echange entre le serveur et client
    Echange(requete,reponse);

    char *ptr = strtok(reponse,"#");
    ptr = strtok(NULL,"#");
    if(strcmp(ptr,"ok") == 0) printf("Login OK. \n");
    else
    {
        ptr = strtok(NULL,"#");
        printf("Erreur de login: %s\n",ptr);
        onContinue=false;
    }

    return onContinue;
}

void MainWindowClientBookEncoder::OBEP_GET_AUTHORS_Client()
{
    char SQL[200]="";
    char nom[50] = "";
    int nbLus;

    clearComboBoxAuthors();
    while(strcmp(SQL,"FINRSQL")!=0)
    {
        memset(SQL,0,sizeof(SQL));// remet à zéro SQL // IMPORTANT CAR LA TAILLE EST MAL LUE SINON
        printf("%s\n",SQL);
        if ((nbLus = Receive(sClient,SQL)) < 0)
        {
            perror("Erreur de Receive");
            ::close(sClient); // il faut les :: pour eviter l'ambiguité entre close de Qt et le close qui ferme la socket !
            exit(1);
        }
        printf("%s\n",SQL);
        char *ptr = strtok(SQL,"#");
        int i = 0;
        while(ptr !=NULL)
        {
            if(i==1)
            {
                strcpy(nom,ptr);
                strcat(nom," ");
            }
            if(i==2)
            {   
                strcat(nom,ptr);
            }
            i++;
            ptr=strtok(NULL,"#");
        }
        if(!strcmp(SQL,"FINRSQL")==0)
        {
            addComboBoxAuthors(nom); // ajout du nom
        }
        
    }
}

void MainWindowClientBookEncoder::OBEP_GET_SUBJECTS_Client()
{
    char SQL[200]="";
    char nom[50];
    int nbLus;

    clearComboBoxSubjects();
    while(strcmp(SQL,"FINRSQL")!=0)
    {
        memset(SQL,0,sizeof(SQL));// remet à zéro SQL // IMPORTANT CAR LA TAILLE EST MAL LUE SINON
        printf("%s\n",SQL);
        if ((nbLus = Receive(sClient,SQL)) < 0)
        {
            perror("Erreur de Receive");
            ::close(sClient); // il faut les :: pour eviter l'ambiguité entre close de Qt et le close qui ferme la socket !
            exit(1);
        }
        printf("%s\n",SQL);
        char *ptr = strtok(SQL,"#");
        int i = 0;
        while(ptr !=NULL)
        {
            if(i==1)
            {
                strcpy(nom,ptr);
            }
            i++;
            ptr=strtok(NULL,"#");
        }
        if(!strcmp(SQL,"FINRSQL")==0)
        {
            addComboBoxSubjects(nom); // ajout du sujet
        }
    }
}
void MainWindowClientBookEncoder::OBEP_GET_BOOKS_Client()
{
    char SQL[200]="";
    char nom[50];
    int nbLus;
    int id, pageCount, publishYear, stockQuantity;
    string title, author, subject, isbn;
    float price;

    clearTableBooks();
    while(strcmp(SQL,"FINRSQL")!=0)
    {
        memset(SQL,0,sizeof(SQL));// remet à zéro SQL // IMPORTANT CAR LA TAILLE EST MAL LUE SINON
        printf("%s\n",SQL);
        if ((nbLus = Receive(sClient,SQL)) < 0)
        {
            perror("Erreur de Receive");
            ::close(sClient); // il faut les :: pour eviter l'ambiguité entre close de Qt et le close qui ferme la socket !
            exit(1);
        }

        printf("%s\n",SQL);
        char *ptr = strtok(SQL,"#");
        int i = 0;
        while(ptr !=NULL)
        {
            if(i==0)
            {
                id = atoi(ptr);
            }
            if(i==1)
            {
                title = ptr;
            }
            if(i==2)
            {
                author = ptr;
            }
            if(i==3)
            {
                subject = ptr;
            }
            if(i==4)
            {
                isbn = ptr;
            }
            if(i==5)
            {
                pageCount = atoi(ptr);
            }
            if(i==6)
            {
                publishYear = atoi(ptr);
            }
            if(i==7)
            {
                price = atof(ptr);
            }
            if(i==8)
            {
                stockQuantity = atoi(ptr);
            }
            i++;
            ptr=strtok(NULL,"#");
        }
        if(!strcmp(SQL,"FINRSQL")==0)
        {
            addTupleTableBooks(id, title, author, subject, isbn, pageCount, publishYear, price, stockQuantity); // ajout des livres
        }
    }   
}
bool OBEP_ADD_AUTHOR_Client(const char* nom, const char* prenom, const char* date)
{
    char requete[200],reponse[200];
    bool onContinue = true;

    // Construction de la requête 
    sprintf(requete,"ADD_AUTHOR#%s#%s#%s",nom,prenom,date);

    // Echange entre le serveur et client
    Echange(requete,reponse);

    char *ptr = strtok(reponse,"#");
    ptr = strtok(NULL,"#");
    if(strcmp(ptr,"ok") == 0) printf("ADD_AUTHOR OK. \n");
    else
    {
        ptr = strtok(NULL,"#");
        printf("Erreur de ADD_AUTHOR: %s\n",ptr);
        onContinue=false;
    }

    return onContinue;
}
bool OBEP_ADD_SUBJECT_Client(const char *nom)
{
    char requete[200],reponse[200];
    bool onContinue = true;

    // Construction de la requête 
    sprintf(requete,"ADD_SUBJECT#%s",nom);

    // Echange entre le serveur et client
    Echange(requete,reponse);

    char *ptr = strtok(reponse,"#");
    ptr = strtok(NULL,"#");
    if(strcmp(ptr,"ok") == 0) printf("ADD_SUBJECT OK. \n");
    else
    {
        ptr = strtok(NULL,"#");
        printf("Erreur de ADD_SUBJECT: %s\n",ptr);
        onContinue=false;
    }

    return onContinue;
}
bool OBEP_ADD_BOOK_Client(const char* titre, const char* isbn, int page ,float prix,int annee,int stock,const char* auteur,const char* sujet)
{
    char requete[200],reponse[200];
    bool onContinue = true;

    // Construction de la requête 
    sprintf(requete,"ADD_BOOK#%s#%s#%d#%f#%d#%d#%s#%s",titre,isbn,page,prix,annee,stock,auteur,sujet);

    // Echange entre le serveur et client
    Echange(requete,reponse);

    char *ptr = strtok(reponse,"#");
    ptr = strtok(NULL,"#");
    if(strcmp(ptr,"ok") == 0)
    {
       printf("ADD_BOOK OK. \n");
       char *p = strtok(NULL,"#"); 
       NbLivre = atoi(p);
    } 
    else
    {
        ptr = strtok(NULL,"#");
        printf("Erreur de ADD_BOOK: %s\n",ptr);
        onContinue=false;
    }

    return onContinue;
}

void OBEP_Logout()
{
    char requete[200],reponse[200];

    // ***** Construction de la requete *********************
    sprintf(requete,"LOGOUT#");
    // ***** Envoi requete + réception réponse **************
    Echange(requete,reponse);
}


bool MainWindowClientBookEncoder::OBEP_Operation(int op)
{
    char requete[200],reponse[200];

    switch(op)
    {
        case 1: 
            sprintf(requete,"GET_AUTHORS#");
            Echange(requete,reponse);
            if(strcmp(reponse,"GET_AUTHORS#ok")==0)
            {
                OBEP_GET_AUTHORS_Client();
                return true;
            }
            return false;
        case 2:
            sprintf(requete,"GET_SUBJECTS#");
            Echange(requete,reponse);
            if(strcmp(reponse,"GET_SUBJECTS#ok")==0)
            {
                OBEP_GET_SUBJECTS_Client();
                return true;
            }
            return false;
        case 3:
            sprintf(requete,"GET_BOOKS#");
            Echange(requete,reponse);
            if(strcmp(reponse,"GET_BOOKS#ok")==0);
            {
                OBEP_GET_BOOKS_Client();
                return true;
            }
            return false;

        default: printf("Erreur lors du switch !");
        return false;
    }
}
// verifie la date encodé au bon format
bool isValidDate(const char* date) {
    // Vérifie la longueur
    if (strlen(date) != 10) return false;

    // Vérifie le format yyyy-mm-dd
    if (date[4] != '-' || date[7] != '-') return false;

    // Vérifie que les années, mois et jours sont des chiffres
    for (int i = 0; i < 10; i++) {
        if (i == 4 || i == 7) continue; // Ignore les tirets
        if (!isdigit(date[i])) return false; // Vérifie que c'est un chiffre
    }

    // Extraire les composants
    int year = atoi(date);
    int month = atoi(date + 5);
    int day = atoi(date + 8);

    // Vérifie les mois et jours valides
    if (month < 1 || month > 12) return false;
    if (day < 1 || day > 31) return false;

    // Vérifie les jours en fonction des mois
    if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) return false;
    if (month == 2) {
        // Vérification pour les années bissextiles
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            if (day > 29) return false; // Février bissextile
        } else {
            if (day > 28) return false; // Février non bissextile
        }
    }

    return true; // Format valide
}
bool verifier_isbn(const char *isbn) 
{
    // Vérifie la longueur de la chaîne
    if (strlen(isbn) != 14) {
        return false; // Longueur incorrecte
    }
    
    // Vérifie les trois premiers chiffres
    for (int i = 0; i < 3; i++) {
        if (!isdigit(isbn[i])) {
            return false; // Ce n'est pas un chiffre
        }
    }

    // Vérifie le tiret
    if (isbn[3] != '-') {
        return false; // Tiret manquant
    }

    // Vérifie les dix chiffres suivants
    for (int i = 4; i < 14; i++) {
        if (!isdigit(isbn[i])) {
            return false; // Ce n'est pas un chiffre
        }
    }

    return true; // Format valide
}

// Définition de échange //
void Echange(char* requete,char* reponse)
{
    int nbEcrits=0, nbLus=0;
    

    // ***** Envoi de la requete ****************************
    if ((nbEcrits = Send(sClient,requete,strlen(requete))) == -1)
    {
        perror("Erreur de Send");
        close(sClient);
        exit(1);
    }
    // ***** Attente de la reponse **************************
    if ((nbLus = Receive(sClient,reponse)) < 0)
    {
        perror("Erreur de Receive");
        close(sClient);
        exit(1);
    }
    if (nbLus == 0)
    {
        printf("Serveur arrete, pas de reponse reçue...\n");
        close(sClient);
        exit(1);
    }
    reponse[nbLus] = 0;
}




