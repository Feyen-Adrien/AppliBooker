(function() {
    console.log("Ceci est une fonction auto-exécutante !");
    miseAJourTable();
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
            alert("Une erreur est survenue...");
        }

    };

    if(document.getElementById('authors-id').value != -1)
    {
        showPopup("Veuillez ne pas entrer d'id lors de l'ajout d'un auteur !")
    }
    else
    {
        xhr.open("POST","http://localhost:8081/authors",true);
        xhr.responseType = "text";
        xhr.setRequestHeader("Content-type", "application/JSON");
        let authorData = {
            idAuthor : null,
            nameAuthor: document.getElementById('authors-name').value,
            firstNameAuthor: document.getElementById('authors-firstname').value,
            dateAuthor: document.getElementById('authors-date').value,
        };
        let Body = JSON.stringify(authorData);//transforme en JSON

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
        let authors;
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.response)
            authors = this.response;
            authors.forEach(function (author) {
                ajouterLigne(author.author_id,author.last_name,author.first_surname,author.author_birthdate);
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

function ajouterLigne(id,nom, prenom, annee)
{
    let tableAuteur = document.getElementById("authors-list");
    // créer un nouvelle ligne
    let nouvelleLigne = document.createElement("tr");
    // créer les cellules
    let celluleId = document.createElement("td");
    celluleId.textContent = id;
    let celluleNom = document.createElement("td");
    celluleNom.textContent = nom;
    let cellulePrenom = document.createElement("td");
    cellulePrenom.textContent = prenom;
    let celluleAnnee = document.createElement("td");
    celluleAnnee.textContent = annee;
    // Ajouter les cellules à la lignes
    nouvelleLigne.appendChild(celluleId);
    nouvelleLigne.appendChild(celluleNom);
    nouvelleLigne.appendChild(cellulePrenom)
    nouvelleLigne.appendChild(celluleAnnee);
    //Ajouter la nouvelle ligne
    tableAuteur.appendChild(nouvelleLigne);
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
