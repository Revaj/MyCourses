#include <conio.h>
#include <stdio.h>

int main(){

    int i, temp = 1000000000;
    char c ;
    int j = 1;

    for(i = 0; i < temp; i++){
        if(kbhit()){
            c = getch();
            if(c == 'I' || c == 'i' && j != 0)
                printf("Interrupcion\n");

            if(c == 'E' || c == 'e' && j != 0)
                printf("Error\n");

            if(c == 'P' || c == 'p' && j != 0){
                printf("Pausa\n");
                j = 0;
            }

            if(c == 'C' || c == 'c' && j == 0){
                j = 1;
                printf("Continua\n");
            }
            if(c == 'S' || c == 's' && j != 0){
                printf("Salir\n");
                break;
            }
        }
    }

    return 0;
}
