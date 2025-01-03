(function() {
    console.log("Ceci est une fonction auto-exécutante !");
    miseAJourTable();
    miseAJourSujet();
    miseAJourAuteur();
})();
// Boutton Ajouter
document.getElementById('add').addEventListener("click",function (e){
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function ()
    {
        console.log(this)
        if(this.readyState == 4 && this.status ==201)
        {
            console.log(this);
            miseAJourTable();
            showPopup(this.responseText);
        }
        else if (this.readyState == 4)
        {
            alert("Une erruer est survenue...");
        }

    };



    if(document.getElementById('book-id').value != -1)
    {
        showPopup("Veuillez ne pas entrer d'id lors de l'ajout d'un livre !")
    }
    else
    {
        xhr.open("POST","http://localhost:8081/books",true);
        xhr.responseType = "text";
        xhr.setRequestHeader("Content-type", "application/JSON");
        let bookData = {
            idBook : null,
            idAuthor: document.getElementById('book-author').value,
            idSubject: document.getElementById('book-subject').value,
            title: document.getElementById('book-name').value,
            isbn: document.getElementById('book-isbn').value,
            pageCount: document.getElementById('book-pages').value,
            stockQuantity: document.getElementById('book-stock').value,
            price: document.getElementById('book-price').value,
            year: document.getElementById('book-year').value,
        };
        let Body = JSON.stringify(bookData);//transforme en JSON

        xhr.send(Body);
    }

})



// fonction utile
function miseAJourTable()
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

    xhr.open("GET","http://localhost:8081/books",true);
    xhr.responseType = "json";
    xhr.send();
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


function ajouterLigne(id,titre,auteur,sujet, ISBN, pages, qte,prix, annee)
{
    let tableLivre = document.getElementById("book-list");
    // créer un nouvelle ligne
    let nouvelleLigne = document.createElement("tr");
    // créer les cellules
    celluleId = document.createElement("td");
    celluleId.textContent = id;
    celluleTitre = document.createElement("td");
    celluleTitre.textContent = titre;
    celluleAuteur = document.createElement("td");
    celluleAuteur.textContent = auteur;
    celluleSujet = document.createElement("td");
    celluleSujet.textContent = sujet;
    celluleISBN = document.createElement("td");
    celluleISBN.textContent = ISBN;
    cellulePages = document.createElement("td");
    cellulePages.textContent = pages;
    celluleQte = document.createElement("td");
    celluleQte.textContent = qte;
    cellulePrix = document.createElement("td");
    cellulePrix.textContent = prix;
    celluleAnnee = document.createElement("td");
    celluleAnnee.textContent = annee;
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
