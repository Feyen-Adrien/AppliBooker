let once = 0;
let id = document.getElementById('sujet-id');
let name = document.getElementById('sujet-name');
document.addEventListener('DOMContentLoaded', function () {
    console.log("La page est prête !");
    miseAJourTable("");
});
// Boutton Ajouter
document.getElementById('add').addEventListener("click",function (e){
    e.preventDefault();
    resetFormErrors();
    if(id.value != null && id.value != "")
    {
        showPopup("Veuillez ne pas entrer d'id lors de l'ajout d'un sujet !");
        id.classList.add("input-error");
    }
    else
    {

        if(name.value !== "")
        {
            let xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function ()
            {
                console.log(this)
                if(this.readyState == 4 && this.status ==201)
                {
                    console.log(this.response);
                    videTable();
                    miseAJourTable("");
                    showPopup(this.responseText);
                    name.value="";
                    id.value="";

                }
                else if(this.readyState == 4)
                {
                    alert("Une erreur est survenue ...");
                }


            };
            xhr.open("POST","http://localhost:8081/subjects",true);
            xhr.responseType = "text";
            xhr.setRequestHeader("Content-Type", "application/json");

            let subjectData = { name : name.value};
            xhr.send(JSON.stringify(subjectData));

        }
        else
        {
            showPopup("Veuillez entrer un nom de sujet !");
            name.classList.add("input-error");
        }
    }
});
// modifier
document.getElementById('update').addEventListener("click",function (e){
    e.preventDefault();
    resetFormErrors()
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function ()
    {
        console.log(this);
        if(this.readyState == 4 && this.status == 200)
        {
            console.log(this.response);
            videTable();
            miseAJourTable("");
            showPopup(this.responseText);
        }
        else if(this.readyState == 4)
        {
            alert("Une erreur est survenue...");
        }
    };
    if(id.value !== "")
    {
        if(name.value !=="")
        {
            let url = "http://192.168.0.170:8081/subjects?id="+id;
            xhr.open("PUT",url,true);
            xhr.responseType = "text";
            xhr.setRequestHeader("Content-Type","application/json");
            let subjectData = { name : name}
            xhr.send(JSON.stringify(subjectData));
            name.value="";
            id.value="";
        }
        else
        {
            showPopup("Veuillez entrez un nom de sujet !");
            name.classList.add("input-error");
        }
    }
    else
    {
        showPopup("Veuillez entrer un id !");
        id.classList.add("input-error");
    }

});
// supprimer
document.getElementById("delete").addEventListener("click",function (e){
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
            miseAJourTable("");
            showPopup(this.responseText);
        }
        else if(this.readyState == 4)
        {
            alert("Une erreur est survenue...");
        }
    };
    if(id.value !== "")
    {
        let url = "http://192.168.0.170:8081/subjects?id="+id;
        xhr.open("DELETE",url,true);
        xhr.responseType = "text";
        xhr.send();
        name.value="";
        id.value="";
    }
    else
    {
        showPopup("Veuillez entrer un id !");
        id.classList.add("input-error");
    }
});
//vider les champs
document.getElementById("clear").addEventListener("click",function (e){
   resetFormErrors();
    name.value="";
    id.value="";
   videTable();
   miseAJourTable("");
});
//rechercher
document.getElementById("search").addEventListener("click",function (e){
    e.preventDefault();
    if(document.getElementById("sujet-id").value !== "" && once ===0)
    {
        showPopup("L'id n'est pas prise en compte lors des recherches.");
        once++
    }
    videTable();
    miseAJourTable(document.getElementById("sujet-name").value);
    name.value="";
    id.value="";
});




// fonction utile
function miseAJourTable(name)
{
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function()
    {
        console.log(this);
        let subjects;
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.response)
            subjects = this.response;
            subjects.forEach(function (subject) {
                ajouterLigne(subject.subject_id, subject.subject_name);
            })
        }
        else if (this.readyState == 4)
        {
            alert("Une erreur est survenue...");
        }
    }
    let url = "http://localhost:8081/subjects";
    if(name != "")
    {
        url += "?name="+name;
    }

    xhr.open("GET",url,true);
    xhr.responseType = "json";
    xhr.send();
}
function videTable()
{
    var maTable = document.getElementById("sujet-list");
    while (maTable.rows.length >= 1) {
        maTable.deleteRow(-1);// supprimer dernière ligne
    }
}

function ajouterLigne(id1, nom)
{
    let tableSujets = document.getElementById("sujet-list");
    // créer un nouvelle ligne
    let nouvelleLigne = document.createElement("tr");
    // créer les cellules
    let celluleId = document.createElement("td");
    celluleId.textContent = id1;
    let celluleNom = document.createElement("td");
    celluleNom.textContent = nom;
    // Ajouter les cellules à la lignes
    nouvelleLigne.appendChild(celluleId);
    nouvelleLigne.appendChild(celluleNom);
    //Ajouter la nouvelle ligne
    tableSujets.appendChild(nouvelleLigne);
    nouvelleLigne.addEventListener('click', function () {
        // Remplir les champs avec les données de la ligne cliquée
        id.value = id1;
        name.value = nom;
    });
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
    id.classList.remove("input-error");
    name.classList.remove("input-error");
}