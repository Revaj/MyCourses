/* Hilos POSIX 

int pthread_create(pthread_t *thread, const pthread_attr_t *attr, void *(*func)(void *),void *arg)
	Crea un proceso ligero que ejecuta "func" con argumento "arg" y atributos "attr".
	Los atributos permiten especificar: tañano de la pila, prioridad, política de planificación etc.
	Existen diversas llamadas para modificar los atributos.

int pthread_join(pthread_t thid, void **value)
	Suspende la ejecución de un proceso ligero hasta que termine el proceso ligero con identificador "thid".
	Devuelve el estado de terminación del proceso ligero.

int pthread_exit(void *value)
	Permite a un proceso ligero finalizar su ejecución, indicando el estado de terminación del mismo.

pthread_t phtread_self(void)
	Devuelve el identificador del thread que ejecuta la llamada

int pthread_attr_setdetachstate(pthread_attr_t *attr, int detachstate)
	Establece el estado de terminación de un proceso ligero.
	Si "detachstate"= PTHREAD_CREATE_DETACHED el proceso ligero liberara sus recursos cuando finalice su ejecución.
	Si "detachstate"= PTHREAD_CREATE_JOINABLE no se liberan los recursos, es necesario utilizar pthread_join().
*/

/*Semáforos POSIX

int sem_init(sem_t *sem, int shared, int val);
	Inicializa un semáforo sin nombre.

int sem_destroy(sem_t *sem);
	Destruye un semáforo sin nombre.

sem_t *sem_open(char *name, int flag, mode_t mode,int val);
	Abre(crea) un semáforo con nombre.

int sem_close(sem_t *sem);
	Cierra un semáforo con nombre.

int sem_unlink(char *name);
	Borra un semáforo con nombre.

int sem_wait(sem_t *sem);
	Realiza la operación wait sobre un semáforo.

int sem_post(sem_t *sem);
	Realiza la operación signal sobre un semáforo.
*/

/*   para compilar es:
gcc direccion/nombre.extension -lpthread -o nombre

     para ejecutar el programa:
./nombre
*/

//Productor-consumidor

#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>
#include <semaphore.h>

#define MAX_BUF	1024	/* tamaño del buffer */
#define DAT_PRO	10000	/* datos a producir */

sem_t elementos;	/* elementos en el buffer */
sem_t huecos;		/* huecos en el buffer */
int buffer[MAX_BUF];	/* buffer comun */

void Productor(void);
void Consumidor(void);
void main(voi){
	pthread_t h1,h2; 	/* identificadores de hilos */
	
	sem_init(&elementos, 0, 0);	/* inicializa semaforos */
	sem_init(&huecos, 0, MAX_BUF);
	
	pthread_create(&h1, NULL,(void*) Productor, NULL); 	/* crear procesos ligeros */
	pthread_create(&h2, NULL,(void*) Consumidor, NULL);

	pthread_join(h1, NULL);	/* esperar su finalización */
	pthread_join(h2, NULL);

	sem_destroy(&huecos);
	sem_destroy(&elementos);
	exit(0);
}

/* código del productor */
void Productor(void){	
	int pos=0;	/* posicición dentro del buffer */
	int dato;	/* dato a producir */
	int i;

	for(i=0; i<DAT_PRO ; i++){
		dato = i;		/* producir dato */
		sem_wait(&huecos);	/* un hueco menos */
		buffer[pos] = i;
		pos = (pos + 1) % MAX_BUF;
		sem_post(&elementos); 	/* un elemento más */
		printf("Productor produce elemento %d \n",elementos);
	}
	pthread_exit(0);
}

/* código del Consumidor */
void Consumidor(void){	
	int pos = 0;
	int dato;
	int i;
	
	for(i=0; i< DAT_PRO ; i++){
			printf("Consumidor consume %d\n",elementos);
		sem_wait(&elementos);	/* un elemento menos */
		dato = buffer[pos];
		pos = (pos+1) % MAX_BUF;
		sem_post(&huecos);	/* un hueco mas  consumir dato*/

	pthread_exit(0);
}
