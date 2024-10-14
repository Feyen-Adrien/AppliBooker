#include "OBEP.h"


int clients[NB_MAX_CLIENTS];
int nbClients = 0;

int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER; // initialiser un mutex mais de manière statique pas dynamique(=> pthread_mutext_init())


bool OBEP(char* requete, char* reponse,int socket,MYSQL* c)
{
	char *ptr = strtok(requete,"#");

	// CAS LOGIN
	if(strcmp(ptr,"LOGIN")== 0)
	{
		char user[50],password[50];
		strcpy(user,strtok(NULL,"#"));
		strcpy(password,strtok(NULL,"#"));
		printf("\t[THREAD %p] LOGIN de %s\n",pthread_self(),user);
		if(estPresent(socket) >= 0)
		{
			sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
		}
		else
		{
			if(OBEP_Login(user,password))
			{
				sprintf(reponse,"LOGIN#ok");
				ajoute(socket);
			}
			else
			{
				sprintf(reponse,"LOGIN#ko#Mauvais identifiants !");
			}
		}
	}
	else
	{
		// CAS	LOGOUT
		if(strcmp(ptr,"LOGOUT") == 0)
		{
			printf("\t[THREAD %p] LOGOUT\n",pthread_self());
			sprintf(reponse,"LOGOUT#ok");
			retire(socket);
		}
		else
		{
			// CAS GET_AUTHORS
			if(strcmp(ptr,"GET_AUTHORS")==0)
			{
				return OBEP_GET_AUTHORS(reponse,c);

			}
			else
			{
				if (strcmp(ptr,"GET_SUBJECTS")==0)
				{
					return OBEP_GET_SUBJECTS(reponse,c);
				}
				else
				{
					if(strcmp(ptr,"GET_BOOKS")==0)
					{
						return OBEP_GET_BOOKS(reponse,c);
					}
					else
					{
						if (strcmp(ptr,"ADD_AUTHOR")==0)
						{
							char n[50],p[50],d[50];
							strcpy(n,strtok(NULL,"#"));
							strcpy(p,strtok(NULL,"#"));
							strcpy(d,strtok(NULL,"#"));
							printf("\t[THREAD %p] ADD_AUTHOR de %s %s\n",pthread_self(),n,p);
							if(OBEP_ADD_AUTHOR(n,p,d,c)==true)
							{
								sprintf(reponse,"ADD_AUTHOR#ok");
							}
						}
						else
						{
							if(strcmp(ptr,"ADD_SUBJECT")==0)
							{
								char n[50];
								strcpy(n,strtok(NULL,"#"));
								printf("\t[THREAD %p] ADD_SUBJECT de %s\n",pthread_self(),n);
								if(OBEP_ADD_SUBJECT(n,c)==true)
								{
									sprintf(reponse,"ADD_SUBJECT#ok");
								}
							}
							else
							{
								if(strcmp(ptr,"ADD_BOOK")==0)
								{
									char t[50],isbn[20],pg[10],prix[10],annee[10],stock[10],auteur[50],sujet[40];
									strcpy(t,strtok(NULL,"#"));
									strcpy(isbn,strtok(NULL,"#"));
									strcpy(pg,strtok(NULL,"#"));
									strcpy(prix,strtok(NULL,"#"));
									strcpy(annee,strtok(NULL,"#"));
									strcpy(stock,strtok(NULL,"#"));
									strcpy(auteur,strtok(NULL,"#"));
									strcpy(sujet,strtok(NULL,"#"));
									printf("\t[THREAD %p] ADD_BOOK  %s\n",pthread_self(),t);
									if(OBEP_ADD_BOOK(t,isbn,pg,prix,annee,stock,auteur,sujet,c)==true)
									{
										my_ulonglong id = mysql_insert_id(c);
										printf("%llu\n",(unsigned long long)id);
										sprintf(reponse,"ADD_BOOK#ok#%llu",(unsigned long long)id);
									}
								}
							}
						}
					}
				}
			}

		}
	}

	

	return true;
}
bool OBEP_Login(const char* user,const char* password)
{
	if(strcmp(user,"adri")==0 && strcmp(password,"123")==0) return true;
	if(strcmp(user,"gaut")==0 && strcmp(password,"456")==0) return true;

	return false;
}


// EN CAS DE FIN PRÉMATURÉ 
void OBEP_Close()
{
	for(int i =0;i<nbClients;i++)
	{
		close(clients[i]);
	}
	pthread_mutex_unlock(&mutexClients);
}

bool OBEP_GET_AUTHORS(char* reponse,MYSQL* c)
{
	char r[] = "SELECT * FROM authors";
	if(mysql_query(c,r) != 0)
	{
		printf("Erreur de mysql_query : %s\n",mysql_error(c));
		exit(1);
	}
	printf("Requête SELECT réussie.\n");
	sprintf(reponse,"GET_AUTHORS#ok");
	return false;
}
bool OBEP_GET_SUBJECTS(char* reponse,MYSQL* c)
{
	char r[] = "SELECT * FROM subjects";
	if(mysql_query(c,r) != 0)
	{
		printf("Erreur de mysql_query : %s\n",mysql_error(c));
		exit(1);
	}
	printf("Requête SELECT réussie.\n");
	sprintf(reponse,"GET_SUBJECTS#ok");
	return false;
}
bool OBEP_GET_BOOKS(char* reponse,MYSQL* c)
{
	char r[] = "SELECT b1.id, b1.title, CONCAT(a1.first_name, ' ', a1.last_name) as author_name, s1.name as subject_name, b1.isbn, b1.page_count, b1.publish_year, b1.price, b1.stock_quantity FROM books b1 INNER JOIN authors a1 on b1.author_id = a1.id INNER JOIN subjects s1 on b1.subject_id = s1.id;";
	if(mysql_query(c,r) != 0)
	{
		printf("Erreur de mysql_query : %s\n",mysql_error(c));
		exit(1);
	}
	printf("Requête SELECT réussie.\n");
	sprintf(reponse,"GET_BOOKS#ok");
	return false;
}
bool OBEP_ADD_AUTHOR(const char* nom, const char* prenom, const char* date,MYSQL* c)
{
	char r[100];

	sprintf(r,"INSERT INTO authors(last_name,first_name,birth_date) values ('%s','%s','%s')",nom,prenom,date);
	if(mysql_query(c,r) != 0)
	{
		printf("Erreur de mysql_query : %s\n",mysql_error(c));
		exit(1);
	}
	printf("Requête INSERT réussie.\n");
	return true; 
}
bool OBEP_ADD_SUBJECT(const char* nom, MYSQL * c)
{
	char r[100];

	sprintf(r,"INSERT INTO subjects(name) values ('%s')",nom);
	if(mysql_query(c,r) != 0)
	{
		printf("Erreur de mysql_query : %s\n",mysql_error(c));
		exit(1);
	}
	printf("Requête INSERT réussie.\n");
	return true; 
}
bool OBEP_ADD_BOOK(const char* titre, const char* isbn, const char* page ,const char* prix,const char* annee,const char* stock,const char* auteur,const char* sujet,MYSQL* c)
{
	char r[500];
	char* pt;
	char np[40];
	char* nom;
	char* prenom;

	float p = strtof(prix,&pt);

	strcpy(np,auteur);

	nom=strtok(np," ");
	prenom=strtok(NULL," ");

	printf("%s\t%s\n",nom,prenom);

	sprintf(r,"INSERT INTO books(author_id,subject_id,title,isbn,page_count,stock_quantity,price,publish_year)  values((SELECT id FROM authors WHERE upper(last_name)=upper('%s') and upper(first_name)=upper('%s')),(SELECT id FROM subjects WHERE upper(name)=upper('%s')),'%s','%s','%d','%d','%.2f','%d')",nom,prenom,sujet,titre,isbn,atoi(page),atoi(stock),p,atoi(annee));
	if(mysql_query(c,r) != 0)
	{
		printf("Erreur de mysql_query : %s\n",mysql_error(c));
		exit(1);
	}
	printf("Requête INSERT réussie.\n");
	return true; 
}



// permet de vérifier la connexion d'un client
int estPresent(int socket)
{
	int indice = -1;
	pthread_mutex_lock(&mutexClients);
	for(int i=0 ; i<nbClients ; i++)
	if (clients[i] == socket) { indice = i; break; }
	pthread_mutex_unlock(&mutexClients);
	return indice;
}

// ajoute le client vient de se connecter
void ajoute(int socket)
{
	pthread_mutex_lock(&mutexClients);
	clients[nbClients] = socket;
	nbClients++;
	pthread_mutex_unlock(&mutexClients);
}

// retire un client qui vient de déconnecter
void retire(int socket)
{
	int pos = estPresent(socket);
	if (pos == -1) return;
	pthread_mutex_lock(&mutexClients);
	for (int i=pos ; i<=nbClients-2 ; i++)
	clients[i] = clients[i+1];
	nbClients--;
	pthread_mutex_unlock(&mutexClients);
}
