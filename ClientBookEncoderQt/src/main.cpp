#include "mainwindowclientbookencoder.h"
#include "TCP.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    SocketLib AppReseau;
    int sSocket = AppReseau.ServerSocket(5000);
    QApplication a(argc, argv);
    MainWindowClientBookEncoder w;
    w.show();

    return a.exec();
}
