cmake_minimum_required(VERSION 3.10)

# Nom du projet
project(Serveur)

# Spécifier le standard C++
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Inclure le répertoire include pour MySQL
include_directories(/usr/include/mysql)

# Ajouter les fichiers sources
set(HEADER_FILES ../ClientBookEncoderQt/include/TCP.h ../ClientBookEncoderQt/include/OBEP.h)
set(SOURCE_FILES ../ClientBookEncoderQt/src/TCP.cpp ../ClientBookEncoderQt/src/OBEP.cpp src/Serveur.cpp)

# Ajouter l'exécutable
add_executable(${PROJECT_NAME} ${SOURCE_FILES} ${HEADER_FILES})

# Lien avec la bibliothèque pthread
find_package(Threads REQUIRED)

# Lier les bibliothèques nécessaires avec le chemin absolu
target_link_libraries(${PROJECT_NAME} 
    PRIVATE 
    ${CMAKE_THREAD_LIBS_INIT} 
    /usr/lib64/mysql/libmysqlclient.so.21  # Chemin absolu vers la bibliothèque
)

# Inclure les fichiers d'en-tête pour le projet client
target_include_directories(${PROJECT_NAME} PRIVATE ../ClientBookEncoderQt/include)
