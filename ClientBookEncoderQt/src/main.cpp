#include "mainwindowclientbookencoder.h"
#include "TCP.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindowClientBookEncoder w;
    w.show();

    return a.exec();
}
