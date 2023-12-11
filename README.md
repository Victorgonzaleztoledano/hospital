# HOSPITAL

Se trata de un sistema de control de citas de un hospital que cuenta con pacientes del cual conocemos su nombre, dni y dirección. Además también tenemos empleados los cuales pueden ser enfermer@s de los cuales conocemos su nombre, dni, dirección y su número de colegiado. Dentro de los empleados también tenemos a los médic@s con todos los atributos anteriores mas los años de experiencia.
Estas son algunas cosas que implementa mi backend:
- Cada empleado tendrá una ventana de trabajo que empieza cada lunes de la semana siguiente a la actual y finaliza el viernes de esa misma semana. Por ejemplo si hoy fuese martes 10 yo no podría pedir cita en la semana en curso tendría que ser a partir del próximo lunes 16 y finaliza el viernes 20. La ventana de horarios se asigna a cada empleado a la hora de agregarlo a la base de datos.
- Se puede comprobar las horas libres de cada empleado para poder crear una cita médica para un paciente. Las horas ya escogidas no aparecerán como disponibles.
- Un paciente no puede tener dos citas médicas a la misma hora ni con el mismo empleado.
- Podrá comprobar las citas que tiene en alta un paciente o un empleado
- Para los médic@s existe una función que comprueba cual es el más ocupado durante la semana en orden ascendente.

### Documentación

La API está documentada con swagger y se podrán realizar llamadas mediante sus endpoints.Para poder correrla solo necesita iniciar el proyecto y usar colocar en su navegador:
~~~
http://localhost:8080/swagger-ui/index.html
~~~

### ALTAS

A continuación voy a dejar un listado de métodos POST y como implementarlos para poder registrar datos en la aplicación.
###### Pacientes
~~~
POST  http://localhost:8080/patients
~~~
Para el endpoint en el que damos de alta un paciente podemos utilizar el siguiente ejemplo en formato JSON:

~~~
{
    "dni": "12345678A",
    "name":"Name",
    "address":"Wall street 24"
}
~~~
Existen excepciones que impiden al cliente crear un DNI que no tenga el formato estandar de 8 dígitos y 1 letra mayúscula al final. Como para el nombre que no podrá registrar un nombre que contenga números o caracteres especiales.

###### Enfermer@s
Para los enfermer@s usaremos este endpoint en el que llamaremos al método POST para agregar un enfermer@

~~~
POST  http://localhost:8080/nurses
~~~
Los empleados tienen su ventana de trabajo que será designada a la hora de su creación. Tendrá su hora de entrada y de salida y para ello utilizaremos un JSON como este ejemplo:
~~~
{
    "dni": "12345678B",
    "address": "123 Main Street",
    "name": "JohnDoe",
    "workingTime": "08:00:00",
    "endWorkingTime": "17:00:00"
}
~~~
Existen restricciones a la hora de crear enfermer@s con datos incoherentes igual que comentaba con los pacientes. Con este JSON podrás crear un enfermer@ sin problemas.

###### Médic@s

Igual que para los enfermer@s utilizaremos un método POST pero usando doctors como keyword.
~~~
POST  http://localhost:8080/doctors
~~~
La particularidad de los médic@s es que para ellos necesitaremos agregar la experiencia a la hora de agregar uno a la base de datos.
~~~
{
    "dni": "12345678A",
    "address": "123 Main Street",
    "name": "JohnDoe",
    "experience": 4,
    "workingTime": "08:00:00",
    "endWorkingTime": "17:00:00"
}
~~~
Por supuesto cuenta con excepciones para evitar que la experiencia sea negativa o se agregue con datos incorrectos.

###### Citas médicas

Para poder dar de alta una cita médica necesitaremos tener dado de alta como mínimo un paciente para poder registrar esa cita a ese paciente mediante su DNI y un empleado ya sea enfermer@ o médic@ del cual usaremos su código de empleado que se genera automáticamente mediante un UUID. Podrá solicitar el código de empleado mediante el método GET del empleado que necesite consultar. Para dar de alta una cita usaremos este método POST:

~~~
POST  http://localhost:8080/appointments
~~~
Recordemos que para dar de alta una cita necesitamos que el día sea uno de la semana siguiente a la semana en curso. Necesitaremos el código de empleado de un empleado y conocer la ventana de trabajo de ese empleado que previamente habremos visualizado y somos conocedores para escoger una hora disponible para esa cita. Un ejemplo de código en formato JSON para el cuerpo de la cita sería:

~~~
{
  "dateAppointment": "2023-12-11",
  "timeAppointment": "09:00:00",
  "dniPatient": "12345678A",
  "codeEmployee": "bee4e218-484e-4a46-a9b7-1099eb9372bf"
}
~~~
Si en algún momento introduce un dato que no exista o no esté disponible será avisado mediante un mensaje de error.

### OTROS ENDPOINTS

A continuación dejo algún ejemplo de otras funcionalidades que tiene la aplicación. Esto es solo una muestra si quieres saber más puedes indagar más profundo en la aplicación.
~~~
GET   http://localhost:8080/nurses/bee4e218-484e-4a46-a9b7-1099eb9372bf/schedule
~~~
Con este método podremos comprobar la ventana de trabajo de un empleado en concreto cuyo código de empleado sería formato UUID para evitar repetidos.

~~~
GET   http://localhost:8080/nurses/bee4e218-484e-4a46-a9b7-1099eb9372bf/appointments
~~~
En este método GET podemos comprobar las citas que tiene asignadas un empleado ordenadas por fecha de más próxima a más lejana.

### EXCEPCIONES
Todas las excepciones que se usan en la aplicación son personalizadas y dan un mejor entendimiento al usuario que la recoge sobre el tipo de error que está sucediendo. Se manejan mediante un global handler que retorna una respuesta dependiendo del tipo de excepción que ocurriese. Aquí tienes un ejemplo de como está implementado:

~~~
@ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
@ResponseBody
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return errors;
    }
~~~

### UML

Por último pongo el diagrama UML que seguí para crear esta aplicación:

![UML](./Curso2023/uml.png)

