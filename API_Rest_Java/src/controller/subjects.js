document.addEventListener('DOMContentLoaded', function () {
    console.log("La page est prête !");
    miseAJourTable("");
});
// Boutton Ajouter
document.getElementById('add').addEventListener("click",function (e){
    e.preventDefault();
    let id = document.getElementById('sujet-id').value;
    if(id != null && id != "")
    {
        showPopup("Veuillez ne pas entrer d'id lors de l'ajout d'un sujet !");
    }
    else
    {
        let name = document.getElementById('sujet-name').value;
        if(name !== "")
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

                }
                else if(this.readyState == 4)
                {
                    alert("Une erreur est survenue ...");
                }


            };
            xhr.open("POST","http://localhost:8081/subjects",true);
            xhr.responseType = "text";
            xhr.setRequestHeader("Content-Type", "application/json");

            let subjectData = { name : name}
            xhr.send(JSON.stringify(subjectData));
            document.getElementById("sujet-id").value="";
            document.getElementById('sujet-name').value="";
        }
        else
        {
            showPopup("Veuillez entrer un nom de sujet !")
        }
    }
});
// modifier
document.getElementById('update').addEventListener("click",function (e){
    e.preventDefault();
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
    let id = document.getElementById("sujet-id").value;
    if(id !== "")
    {
        let name = document.getElementById("sujet-name").value;
        if(name !=="")
        {
            let url = "http://192.168.0.170:8081/subjects?id="+id;
            xhr.open("PUT",url,true);
            xhr.responseType = "text";
            xhr.setRequestHeader("Content-Type","application/json");
            let subjectData = { name : name}
            xhr.send(JSON.stringify(subjectData));
            document.getElementById("sujet-id").value="";
            document.getElementById('sujet-name').value="";
        }
        else
        {
            showPopup("Veuillez entrez un nom de sujet !");
        }
    }
    else
    {
        showPopup("Veuillez entrer un id !");
    }

});
// supprimer
document.getElementById("delete").addEventListener("click",function (e){
    e.preventDefault();

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
    let id = document.getElementById("sujet-id").value;
    if(id !== "")
    {
        let url = "http://192.168.0.170:8081/subjects?id="+id;
        xhr.open("DELETE",url,true);
        xhr.responseType = "text";
        xhr.send();
        document.getElementById("sujet-id").value="";
        document.getElementById('sujet-name').value="";
    }
    else
    {
        showPopup("Veuillez entrer un id !")
    }
});
//vider les champs
document.getElementById("clear").addEventListener("click",function (e){
   document.getElementById("sujet-id").value="";
   document.getElementById("sujet-name").value="";
   videTable();
   miseAJourTable("");
});
//rechercher
document.getElementById("search").addEventListener("click",function (e){
    e.preventDefault();
    videTable();
    miseAJourTable(document.getElementById("sujet-name").value);
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
    let url = "http://192.168.0.170:8081/subjects";
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

function ajouterLigne(id, nom)
{
    let tableSujets = document.getElementById("sujet-list");
    // créer un nouvelle ligne
    let nouvelleLigne = document.createElement("tr");
    // créer les cellules
    let celluleId = document.createElement("td");
    celluleId.textContent = id;
    let celluleNom = document.createElement("td");
    celluleNom.textContent = nom;
    // Ajouter les cellules à la lignes
    nouvelleLigne.appendChild(celluleId);
    nouvelleLigne.appendChild(celluleNom);
    //Ajouter la nouvelle ligne
    tableSujets.appendChild(nouvelleLigne);
    nouvelleLigne.addEventListener('click', function () {
        // Remplir les champs avec les données de la ligne cliquée
        document.getElementById('sujet-id').value = id;
        document.getElementById('sujet-name').value = nom;
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
