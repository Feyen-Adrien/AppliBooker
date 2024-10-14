#ifndef OBEP_H
#define OBEP_H

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <string.h>
#include <mysql.h>
#include <time.h>
#include <ctype.h>
#include "TCP.h"


#define NB_MAX_CLIENTS 100


bool OBEP(char* requete, char* reponse,int socket,MYSQL* c);
bool OBEP_Login(const char* user,const char* password);
bool OBEP_GET_AUTHORS(char* reponse,MYSQL* c);
bool OBEP_GET_SUBJECTS(char* reponse,MYSQL* c);
bool OBEP_GET_BOOKS(char* reponse,MYSQL* c);
bool OBEP_ADD_AUTHOR(const char* nom, const char* prenom, const char* date,MYSQL* c);
bool OBEP_ADD_SUBJECT(const char* nom,MYSQL* c);
bool OBEP_ADD_BOOK(const char* titre, const char* isbn, const char* page ,const char* prix,const char* annee,const char* stock,const char* auteur,const char* sujet,MYSQL* c);
void OBEP_Close();

#endif