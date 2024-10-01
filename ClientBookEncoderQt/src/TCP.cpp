#include "TCP.h"

//---------------------  CONSTRUCTEURS --------------------------------

SocketLib::SocketLib()
{
	#ifdef DEBUG
		cout << "Constructeur par defaut ! (SocketLib)" << endl;
	#endif
}

//---------------------  DESTRUCTEUR  ----------------------------------

SocketLib::~SocketLib()
{
	#ifdef DEBUG
		cout << "Destructeur ! (SocketLib)" << endl;
	#endif
}

//-------------------- AUTRES METHODES ---------------------------------
int SocketLib::ServerSocket(int bearing)
{
	// création de la socket
	int s;
	char const *p = to_string(bearing).c_str();

	printf("pid = %d\n",getpid());

	// création de la socket
	if ((s = socket(AF_INET, SOCK_STREAM, 0)) == -1) 
	{
		perror("Erreur de socket()");
		exit(1);
	}
	printf("socket creee = %d\n",s);


	// Construction de l'adresse
	struct addrinfo hints;
	struct addrinfo *results;
	memset(&hints,0,sizeof(struct addrinfo));
	hints.ai_family=AF_INET; // => protocole fondé sur IPv4
	hints.ai_socktype=SOCK_STREAM; //=> car mode connecté (TCP)
	hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV; // pour une connexion passive
	if(getaddrinfo(NULL,p,&hints,&results) != 0) // mettre NULL car on crée un SERVEUR ICI ip => 0.0.0.0 et le port souahité
	{
		printf("erreur getaddrinfo");
		exit(1);
	}
	
	// Affichage du contenu de l'adresse obtenue
	char host[NI_MAXHOST];
	char port[NI_MAXSERV];

	getnameinfo(results->ai_addr,results->ai_addrlen,
	host,NI_MAXHOST,port,NI_MAXSERV,NI_NUMERICSERV | NI_NUMERICHOST);
	printf("Mon Adresse IP: %s -- Mon Port: %s\n",host,port);
	printf("Hote: %s -- Service: %s\n",host,port);


	// Liaison de la socket à l'adresse
	if (bind(s,results->ai_addr,results->ai_addrlen) < 0)
	{
	 	perror("Erreur de bind()");
	 	exit(1);
	}
	freeaddrinfo(results);
	printf("bind() reussi !\n");

	return s; // renvoi la socket
}


