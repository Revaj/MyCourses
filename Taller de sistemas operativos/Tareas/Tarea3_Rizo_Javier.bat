 @echo off
 :: Esto nos ayuda a que la instruccio no repita la direccion de la ruta donde esta nuestro bat
 echo Tarea 3 de Rizo Orozco Javier Agustin :D 
 :: Esta instrucción imprime en pantalla el texto que sigue despues de su instrucción
 pause 
 ::Esta instrucción nos ayuda a pausar la ejecución del codigo
 @echo on
 :: Esto nos ayuda a que muestre la ruta porque usare el siguiente comando
 cd ..
 :: cd nos permite dirigirnos a una ruta de nuestras carpetas, al usar con dos puntos se  dirige a la carpeta anterior a el
 pause
 ::Vuelve a pausar para mostrar el resultado 
 @echo off
 ::Ocultamos la ruta de nuevo
 echo Si aprieta cualquier tecla se usara la funcion cls que permitira limpiar la pantalla de consola
 pause
 :: Pausamos antes de limpiar la pantalla de consola
 cls
 :: Limpia la pantalla de consola dejandola con fondo negro de nuevo
 echo la pantalla se limpio, aunque la este empezando a usar otra vez
 :: Mostramos un mensaje de nuevo en pantalla
 pause
 :: Pausamos antes de que salga
 Title Ya casi acabamos
 pause
 echo Ahora cambio el titulo ya que se ve algo aburrido mostrando una direccion, eso no me dice mucho. Title nos servira para poder cambiar el titulo del batch
 pause
 echo Mostrare la ruta y lo que contiene en ella
 pause
 @echo on
 dir
 @echo off
 echo Como podemos ver usando dir nos permite visualizar el contenido de nuestra ruta.
 pause
 color 20
 echo Creo un poco de color no le caeria mal. Tambien tiene funciones de colores un bat para poder cambiar el tipico fondo oscuro a uno mejor.
 pause
 echo Por ultimo hagamos una operacion, creare dos variables  A y B, A tendra un valor de 5 y B tendra un valor de 15, el resultado debera ser 20
 echo Demos un enter para ver el resultado :D
 pause > nul
 :: Usamos la instruccion para que no muestre el tipico mensaje de Presione una tecla para continuar, sino que mostrara solo un guion bajo parpadeando en espera de que se presione alguna tecla
 set A=5
 set B=15
 set /a resultado= %A% + %B%
 :: set nos permite declarar una variable como si fuera un lenguaje de programacion, sin tener que saber de que tipo de dato va guardar y el porcentaje nos permite usar esas variables con las demas instrucciones del bat
 :: /a nos permitira hacer operaciones aritmeticas con la variable
 echo %resultado%
 echo Bueno eso es todo maestra :D!!
 pause > nul
 exit
 :: si bien sale nuestro programa sin tener que usar el exit, puede servir para hacer operaciones como mandar un mensaje de error y terminar, detener un servicio o cualquier otro ejemplo que pueda servir
 :: En general los bats si bien pueden no parecer tan utiles, hacer uno pequeño y completementarlo a nuestros programas
 :: puede servirnos para ejecutar cosas pequeñas que puedan parecer dificiles en algunos lenguajes pero para el bat 
 :: le sera sencillo.
 :: Existen tambien otras instrucciones como la creacion de carpetas y archivos aunque no quise realizarlas como algo > algo.txt o mkdir ya que no queria crear basura por asi decirlo  :)