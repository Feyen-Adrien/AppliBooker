let once = 0;
let id = document.getElementById('authors-id');
let lastName = document.getElementById('authors-name');
let firstName = document.getElementById('authors-firstname');
let birthDate = document.getElementById('authors-date');
document.addEventListener('DOMContentLoaded', function () {
    console.log("La page est prête !");
    miseAJourTable("","");
});
// Boutton Ajouter
document.getElementById('add').addEventListener("click",function (e){
    e.preventDefault();
    resetFormErrors();
    if(id.value != null && id.value != "")
    {
        showPopup("Veuillez ne pas entrer d'id lors de l'ajout d'un auteur !");
        id.classList.add("input-error");
    }
    else
    {
        let err=0;
        if(lastName.value== "")
        {
            err=1;
            lastName.classList.add("input-error");
        }
        if(firstName.value == "")
        {
            err=1;
            firstName.classList.add("input-error");
        }
        if (birthDate.value =="")
        {
            err=1;
            birthDate.classList.add("input-error");
        }
        else
        {
            if(verifierFormatDate(birthDate.value)==false)
            {
                err=2;
                birthDate.classList.add("input-error");
            }
        }
        if(err == 0)
        {
            let xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function ()
            {
                console.log(this)
                if(this.readyState == 4 && this.status ==201)
                {
                    console.log(this);
                    videTable();
                    miseAJourTable("","");
                    showPopup(this.responseText);
                    viderInput();
                }
                else if (this.readyState == 4)
                {
                    alert("Une erruer est survenue...");
                }

            };
            xhr.open("POST","http://localhost:8081/authors",true);
            xhr.responseType = "text";
            xhr.setRequestHeader("Content-type", "application/JSON");
            let authorData = {
                lastname: lastName.value,
                firstname: firstName.value,
                birthdate: birthDate.value,
            };
            let Body = JSON.stringify(authorData);//transforme en JSON

            xhr.send(Body);
        }
        else
        {
            if(err==1)
            {
                showPopup("Veuillez entrer des données dans les endroits rouges");
            }
            else
            {
                if (err ==2)
                {
                    showPopup("Veuillez entrer une date valide et au format YYYY-MM-JJ");
                }
            }
        }
    }
})
document.getElementById('update').addEventListener("click", function (e){
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
            miseAJourTable("","");
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
        if(lastName.value != "")
        {
            if(firstName.value != "")
            {
                if(verifierFormatDate(birthDate.value))
                {
                    let url = "http://localhost:8081/authors?id="+id.value;
                    xhr.open("PUT",url,true);
                    xhr.responseType = "text";
                    xhr.setRequestHeader("Content-Type","application/json");
                    let authorData = {
                        lastname: lastName.value,
                        firstname: firstName.value,
                        birthdate: birthDate.value,
                    };
                    xhr.send(JSON.stringify(authorData));
                }
                else
                {
                    showPopup("La date entrée est invalide ou bien ne respecte pas le format YYYY-MM-JJ");
                    birthDate.classList.add("input-error");
                }
            }
            else
            {
                showPopup("Veuillez entrer un prénom");
                firstName.classList.add("input-error");
            }
        }
        else
        {
            showPopup("Veuillez entrer un nom");
            lastName.classList.add("input-error");
        }
    }
    else
    {
        showPopup("Veuillez entrer un id de d'auteur");
        id.classList.add("input-error");
    }
})
document.getElementById('delete').addEventListener("click", function (e){
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
            miseAJourTable("","");
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
        let url = "http://localhost:8081/authors?id="+id.value;
        xhr.open("DELETE",url,true);
        xhr.responseType = "text";
        xhr.send();
    }
    else
    {
        showPopup("Veuillez entrer un id !");
        id.classList.add("input-error");
    }
})
document.getElementById('search').addEventListener("click", function (e){
    e.preventDefault();
    if(once ===0)
    {
        showPopup("Lors des recherches les éléments suivants ne sont pas pris en compte : id et date de naissance !");
        once++
    }
    videTable();
    miseAJourTable(lastName.value,firstName.value);
})
document.getElementById('clear').addEventListener("click", function (e){
    resetFormErrors();
    viderInput();
    videTable();
    miseAJourTable("","");
})



// fonction utile
function miseAJourTable(lastname, firstname)
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
    let url = "http://localhost:8081/authors?";
    if(lastname != "")
    {
        url += "lastName="+lastname;
    }
    if(firstname != "")
    {
        url += "&firstName="+firstname;
    }
    xhr.open("GET",url,true);
    xhr.responseType = "json";
    xhr.send();
}

function ajouterLigne(id1,nom, prenom, annee)
{
    let tableAuteur = document.getElementById("authors-list");
    // créer un nouvelle ligne
    let nouvelleLigne = document.createElement("tr");
    // créer les cellules
    let celluleId = document.createElement("td");
    celluleId.textContent = id1;
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
    nouvelleLigne.addEventListener('click', function () {
        // Remplir les champs avec les données de la ligne cliquée
        id.value = id1;
        lastName.value = nom;
        firstName.value = prenom;
        birthDate.value= annee;
    });
    tableAuteur.appendChild(nouvelleLigne);
}
function videTable()
{
    var maTable = document.getElementById("authors-list");
    while (maTable.rows.length >= 1) {
        maTable.deleteRow(-1);// supprimer dernière ligne
    }
}
function viderInput()
{
    id.value="";
    lastName.value="";
    firstName.value="";
    birthDate.value="";
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

//pour les erreurs
function resetFormErrors() {
    // Supprimer les bordures rouges des champs
    id.classList.remove("input-error");
    lastName.classList.remove("input-error");
    firstName.classList.remove("input-error");
    birthDate.classList.remove("input-error");
}

// date verif
function verifierFormatDate(date) {
    // Expression régulière pour vérifier le format YYYY-MM-JJ
    const regex = /^\d{4}-\d{2}-\d{2}$/;

    // Vérifie si la date correspond au format
    if (!regex.test(date)) {
        return false;
    }

    // Sépare la date en année, mois et jour
    const [annee, mois, jour] = date.split('-').map(Number);

    // Vérifie si l'année est valide
    if (annee < 1000 || annee > 9999) {
        return false;
    }

    // Vérifie si le mois est valide (1 à 12)
    if (mois < 1 || mois > 12) {
        return false;
    }

    // Vérifie si le jour est valide en fonction du mois
    const joursParMois = [31, (annee % 4 === 0 && (annee % 100 !== 0 || annee % 400 === 0)) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    if (jour < 1 || jour > joursParMois[mois - 1]) {
        return false;
    }

    return true;
}

