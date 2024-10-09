#include "OBEP.h"


int clients[NB_MAX_CLIENTS];
int nbClients = 0;

int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER; // initialiser un mutex mais de manière statique pas dynamique(=> pthread_mutext_init())


bool OBEP(char* requete, char* reponse,int socket)
{
	char *ptr = strtok(requete,"#");

	// en cas de LOGIN
	if(strcmp(ptr,"LOGIN")== 0)
	{
		char user[50],password[50];
		strcpy(user,strtok(NULL,"#"));
		strcpy(password,strtok(NULL,"#"));
		printf("%\t[THREAD %p] LOGIN de %s\n",pthread_self(),user);
		if(estPresent(socket) >= 0)
		{
			sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
			return false;
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
				return false;
			}
		}
	}

	// CAS DE LOGOUT
	if(strcmp(reponse,"LOGOUT") == 0)
	{
		printf("%\t[THREAD %p] LOGOUT\n",pthread_self());
		retire(socket);
		sprintf(reponse,"LOGOUT#ok");
		return false;
	}
	return  true;
}
bool OBEP_Login(const char* user,const char* password)
{
	if(strcmp(user,"adri")==0 && strcmp(password,"123")==0) return true;
	// requete sql ici normalement pour accèes bd
	return false;
}
int OBEP_Operation(char op,int a,int b)
{
	// acces bd requete sql
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
