#include <sys/types.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>

void funcion_hilo( char *a ) {
	printf( "%s", a );	
	sleep( 3 );
}

int main( void ) {
              pthread_t hilo;
              int res;
              char a[] = "Hola mundo\n";
              res = pthread_create( &hilo, NULL, (void*) funcion_hilo, a );
	if( res = 0 ) {
		printf( "Error al crear hilo" );
		exit( 0 );
	}
	printf( "Esperando al hilo..." );
	res = pthread_join( hilo, NULL );
	if( res != 0 ) {
		printf( "Error al terminar hilo" );
		exit(0);
	}
	printf( "Terminó proceso padre\n" );
	

}
