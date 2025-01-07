let once =0;
let id=document.getElementById("book-id");
let idAuteur=document.getElementById("book-author");
let idSubject =document.getElementById("book-subject");
let titre=document.getElementById("book-name");
let isbn=document.getElementById("book-isbn");
let qte=document.getElementById("book-stock");
let nbPage =document.getElementById("book-pages");
let price =document.getElementById("book-price");
let annee =document.getElementById("book-year");
document.addEventListener('DOMContentLoaded', function () {
    console.log("La page est prête !");
    miseAJourTable("","","","");
    miseAJourAuteur();
    miseAJourSujet();
});
// Boutton Ajouter
document.getElementById('add').addEventListener("click",function (e){
    e.preventDefault();
    resetFormErrors();
    if(id.value != null && id.value != "")
    {
        showPopup("Veuillez ne pas entrer d'id lors de l'ajout d'un livre !");
        id.classList.add("input-error");
    }
    else
    {
        let err = 0;
        let err2 = 0;
        if(idAuteur.value =="-1")
        {
            idAuteur.classList.add("input-error");
            err=1;
        }
        if(idSubject.value =="-1")
        {
            idSubject.classList.add("input-error");
            err=1;
        }
        if(titre.value =="")
        {
            titre.classList.add("input-error");
            err=1;
        }
        if(isValidISBN(isbn.value)==false)
        {
            isbn.classList.add("input-error");
            err2=1;
        }
        else
        {
            if(isbn.value =="")
            {
                err=1;
            }
            isbn.classList.add("error-input");
        }
        if(qte.value =="")
        {
            qte.classList.add("input-error");
            err=1;
        }
        if(nbPage.value =="")
        {
            nbPage.classList.add("input-error");
            err=1;
        }
        if(price.value =="")
        {
            price.classList.add("input-error");
            err=1;
        }
        if(annee.value =="")
        {
            annee.classList.add("input-error");
            err=1;
        }
        if(err2==1)
        {
            showPopup("Veuilllez respecter le format ISBN (13 chiffres) : XXX-XXXXXXXXXX")
        }
        if(err==1)
        {
            showPopup("Veuillez indiquer des valeurs aux endroits rouges");
        }


        if(err==0 && err2 ==0)
        {
            let xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function ()
            {
                console.log(this)
                if(this.readyState == 4 && this.status ==201)
                {
                    console.log(this);
                    videTable();
                    miseAJourTable("","","","");
                    showPopup(this.responseText);
                    viderInput();
                }
                else if (this.readyState == 4)
                {
                    alert("Une erruer est survenue...");
                }

            };
            xhr.open("POST","http://localhost:8081/books",true);
            xhr.responseType = "text";
            xhr.setRequestHeader("Content-type", "application/JSON");
            let bookData = {
                idAuthor: idAuteur.value,
                idSubject: idSubject.value,
                title: titre.value,
                isbn: isbn.value,
                stockQuantity: qte.value,
                pageCount: nbPage.value,
                price: price.value,
                year: annee.value,
            };
            let Body = JSON.stringify(bookData);//transforme en JSON

            xhr.send(Body);
        }
    }
});
document.getElementById('update').addEventListener("click",function(e){
    e.preventDefault();
    resetFormErrors();
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function ()
    {
        console.log(this);
        if(this.readyState == 4 && this.status == 200)
        {
            console.log(this.response);
            videTable();
            miseAJourTable("","","","");
            showPopup(this.responseText);
            viderInput();

        }
        else if(this.readyState == 4)
        {
            alert("Une erreur est survenue...");
        }
    };
    if(id.value != "")
    {
        if(idAuteur.value != "-1")
        {
            if(idSubject.value != "-1")
            {
                if(titre.value != "")
                {
                    if(isValidISBN(isbn.value))
                    {
                        if(qte.value !="" && parseInt(qte.value)>0)
                        {
                            if(nbPage.value != "" && parseInt(nbPage.value)>0)
                            {
                                if(price.value !="" && parseFloat(price.value)>0.0)
                                {
                                    if(annee.value !="" && parseInt(annee.value)>0)
                                    {
                                        let url = "http://localhost:8081/books?id="+id.value;
                                        xhr.open("PUT",url,true);
                                        xhr.responseType = "text";
                                        xhr.setRequestHeader("Content-Type","application/json");
                                        let bookData = {
                                            idAuthor: idAuteur.value,
                                            idSubject: idSubject.value,
                                            title: titre.value,
                                            isbn: isbn.value,
                                            stockQuantity: qte.value,
                                            pageCount: nbPage.value,
                                            price: price.value,
                                            year: annee.value,
                                        };
                                        xhr.send(JSON.stringify(bookData));
                                    }
                                    else
                                    {
                                        showPopup("Veuillez entrer un année(>0)");
                                        annee.classList.add("input-error");
                                    }
                                }
                                else
                                {
                                    showPopup("Veuillez entrer un prix(>0)");
                                    price.classList.add("input-error");
                                }
                            }
                            else
                            {
                                showPopup("Veuillez entrer le nombre de page(>0)");
                                nbPage.classList.add("input-error");
                            }
                        }
                        else
                        {
                            showPopup("Veuillez entrer une quantité de stock(>0)");
                            qte.classList.add("input-error");
                        }
                    }
                    else
                    {
                        showPopup("Format d'ISBN invalid");
                        isbn.classList.add("input-error");
                    }
                }
                else
                {
                    showPopup("Veuillez entrer un titre");
                    titre.classList.add("input-error");
                }
            }
            else
            {
                showPopup("Veuillez selectionner un sujet");
                idSubject.classList.add("input-error");
            }
        }
        else
        {
            showPopup("Veuillez selectionnr un auteur");
            idAuteur.classList.add("input-error");
        }
    }
    else
    {
        showPopup("Veuillez entrer un id de livre");
        id.classList.add("input-error");
    }
});
document.getElementById('delete').addEventListener("click",function (e){
    e.preventDefault();
    resetFormErrors();
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function ()
    {
        console.log(this);
        if(this.readyState == 4 && this.status == 200)
        {
            console.log(this.response);
            videTable();
            miseAJourTable("","","","");
            showPopup(this.responseText);
            viderInput();
        }
        else if(this.readyState == 4)
        {
            alert("Une erreur est survenue...");
        }
    };
    if(id.value !== "")
    {
        let url = "http://localhost:8081/books?id="+id.value;
        xhr.open("DELETE",url,true);
        xhr.responseType = "text";
        xhr.send();
    }
    else
    {
        showPopup("Veuillez entrer un id !");
        id.classList.add("input-error");
    }
});
document.getElementById('search').addEventListener("click",function (e){
    e.preventDefault();
    if(once ===0)
    {
        showPopup("Lors des recherches les éléments suivants ne sont pas pris en compte : id,ISBN,Quantité en stock,nombre de pages et l'année de publication");
        once++
    }
    videTable();
    miseAJourTable(idAuteur.value,idSubject.value,titre.value,price.value);
});
document.getElementById('clear').addEventListener("click",function (e){
    resetFormErrors();
    viderInput();
    videTable();
    miseAJourTable("","","","")
});



// fonction utile
function miseAJourTable(idAuteur,idSujet,titre,prix)
{
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function()
    {
        console.log(this);
        let books;
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.response)
            books = this.response;
            books.forEach(function (book) {
                ajouterLigne(book.id, book.title, book.author_id, book.subject_id,book.isbn,book.page_count,book.stock_quantity,book.price,book.publish_year);
            })
        }
        else if (this.readyState == 4)
        {
            alert("Une erreur est survenue...");
        }
    }
    let url = "http://localhost:8081/books";
    if(idAuteur !=="")
    {
        url += "?lastName="+idAuteur;
    }
    if(idSujet !=="")
    {
        url += "&subject="+idSujet;
    }
    if(titre !=="")
    {
        url += "&title="+titre;
    }
    if(prix !=="")
    {
        url += "&price="+prix;
    }
    xhr.open("GET",url,true);
    xhr.responseType = "json";
    xhr.send();
}
function videTable()
{
    var maTable = document.getElementById("book-list");
    while (maTable.rows.length >= 1) {
        maTable.deleteRow(-1);// supprimer dernière ligne
    }
}
function miseAJourAuteur()
{
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function()
    {
        console.log(this);
        let auteurs;
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.response)
            auteurs = this.response;
            auteurs.forEach(function (auteur) {
                ajouterAuteur(auteur.author_id,auteur.last_name,auteur.first_surname);
            })
        }
        else if (this.readyState == 4)
        {
            alert("Une erreur est survenue...");
        }
    }

    xhr.open("GET","http://localhost:8081/authors",true);
    xhr.responseType = "json";
    xhr.send();
}
function miseAJourSujet()
{
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function()
    {
        console.log(this);
        let sujets;
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.response)
            sujets = this.response;
            sujets.forEach(function (sujet) {
                ajouterSujet(sujet.subject_id, sujet.subject_name);
            })
        }
        else if (this.readyState == 4)
        {
            alert("Une erreur est survenue...");
        }
    }

    xhr.open("GET","http://localhost:8081/subjects",true);
    xhr.responseType = "json";
    xhr.send();
}
function viderInput()
{
    idAuteur.value="-1";
    idSubject.value="-1";
    titre.value="";
    isbn.value="";
    qte.value="";
    nbPage.value="";
    price.value="";
    annee.value="";
}


function ajouterLigne(ide,titre1,auteur,sujet, ISBN, pages, qte1,prix, annee1)
{
    let tableLivre = document.getElementById("book-list");
    // créer un nouvelle ligne
    let nouvelleLigne = document.createElement("tr");
    // créer les cellules
    celluleId = document.createElement("td");
    celluleId.textContent = ide;
    celluleTitre = document.createElement("td");
    celluleTitre.textContent = titre1;
    celluleAuteur = document.createElement("td");
    celluleAuteur.textContent = auteur;
    celluleSujet = document.createElement("td");
    celluleSujet.textContent = sujet;
    celluleISBN = document.createElement("td");
    celluleISBN.textContent = ISBN;
    cellulePages = document.createElement("td");
    cellulePages.textContent = pages;
    celluleQte = document.createElement("td");
    celluleQte.textContent = qte1;
    cellulePrix = document.createElement("td");
    cellulePrix.textContent = prix;
    celluleAnnee = document.createElement("td");
    celluleAnnee.textContent = annee1;
    // Ajouter les cellules à la lignes
    nouvelleLigne.appendChild(celluleId);
    nouvelleLigne.appendChild(celluleTitre);
    nouvelleLigne.appendChild(celluleAuteur);
    nouvelleLigne.appendChild(celluleSujet);
    nouvelleLigne.appendChild(celluleISBN);
    nouvelleLigne.appendChild(cellulePages);
    nouvelleLigne.appendChild(celluleQte);
    nouvelleLigne.appendChild(cellulePrix);
    nouvelleLigne.appendChild(celluleAnnee);
    //Ajout Listener pour être selectionnable
    nouvelleLigne.addEventListener('click', function () {
        // Remplir les champs avec les données de la ligne cliquée
        id.value = ide;
        idAuteur.value = getValueByText(idAuteur,auteur);
        idSubject.value = getValueByText(idSubject,sujet);
        titre.value = titre1;
        isbn.value = ISBN;
        nbPage.value = pages;
        qte.value = qte1;
        price.value =prix;
        annee.value = annee1;
    });
    //Ajouter la nouvelle ligne
    tableLivre.appendChild(nouvelleLigne);

}
// pour afficher les différents auteurs et sujets(liste déroulantes)
function ajouterAuteur(idAuteur,AuteurNom, AuteurPrenom)
{
    let liste = document.getElementById("book-author");
    let ligne = document.createElement('option');
    ligne.value = idAuteur;
    ligne.textContent = AuteurNom+ " " + AuteurPrenom;
    liste.appendChild(ligne);
}
function ajouterSujet(idsujet,sujet)
{
    let liste = document.getElementById("book-subject");
    let ligne = document.createElement('option');
    ligne.value = idsujet;
    ligne.textContent = sujet;
    liste.appendChild(ligne);
}


// pour le popup
// Fonction pour afficher le popup
function showPopup(message) {
    const popup = document.getElementById('popup');
    const popupMessage = document.getElementById('popup-message');
    popupMessage.textContent = message; // Modifier le message du popup
    popup.style.display = 'flex'; // Afficher le popup
}

// Fonction pour fermer le popup
function closePopup() {
    const popup = document.getElementById('popup');
    popup.style.display = 'none'; // Cacher le popup
}
// Écouteur d'événement pour fermer le popup lorsqu'on clique sur la croix
document.getElementById('popup-close').addEventListener('click', closePopup);

// Fonction pour réinitialiser les erreurs du formulaire
function resetFormErrors() {
    // Supprimer les bordures rouges des champs
    id.classList.remove('input-error');
    idAuteur.classList.remove('input-error');
    idSubject.classList.remove('input-error');
    titre.classList.remove('input-error');
    isbn.classList.remove('input-error');
    qte.classList.remove('input-error');
    nbPage.classList.remove('input-error');
    price.classList.remove('input-error');
    annee.classList.remove('input-error');
}
function isValidISBN(isbn) {
    // Expression régulière pour vérifier le format "XXX-XXXXXXXXXX"
    const regex = /^\d{3}-\d{10}$/;

    // Teste le format de l'ISBN
    return regex.test(isbn);
}
function getValueByText(select,text) {
    let options = select.options; // Récupère toutes les options du <select>

    for (var i = 0; i < options.length; i++) {
        if (options[i].text === text) {
            return options[i].value;  // Retourne la valeur de l'option
        }
    }
    return null;  // Si le texte n'a pas été trouvé, retourne null
}

