cmake_minimum_required(VERSION 3.10)

#Nom du projet
project(Serveur)

#Spécifier le standard C++
set (CMAKE_CXX_STANDARD 11)
set (CMAKE_CXX_STANDARD_REQUIRED True)

#Inclure le répertoire include
set(HEADER_FILES ../ClientBookEncoderQt/include/TCP.h ../ClientBookEncoderQt/include/OBEP.h)

#Ajouter les fichiers sources automatiquement
set(SOURCE_FILES ../ClientBookEncoderQt/src/TCP.cpp ../ClientBookEncoderQt/src/OBEP.cpp src/Serveur.cpp)

#Ajouter l'exécutable 
add_executable(${PROJECT_NAME} ${SOURCE_FILES} ${HEADER_FILES})

target_include_directories(${PROJECT_NAME} PRIVATE ../ClientBookEncoderQt/include)

#Lien avec la bibliothèque pthread
find_package(Threads REQUIRED)
target_link_libraries(${PROJECT_NAME} PRIVATE Threads::Threads)




