#### Escuela Colombiana de Ingeniería
#### Procesos de desarrollo de software - PDSW
#### Construción de un cliente 'grueso' con un API REST, HTML5, Javascript y CSS3. Parte II.

-----------------------------

## Realizado Por:
* Nicolás Sebastian Achuri Macias
* Ricardo Andres Villamizar Mendez

------------------------------



![](img/mock2.png)

1. Agregue al canvas de la página un manejador de eventos que permita capturar los 'clicks' realizados, bien sea a través del mouse, o a través de una pantalla táctil. Para esto, tenga en cuenta [este ejemplo de uso de los eventos de tipo 'PointerEvent'](https://mobiforge.com/design-development/html5-pointer-events-api-combining-touch-mouse-and-pen) (aún no soportado por todos los navegadores) para este fin. Recuerde que a diferencia del ejemplo anterior (donde el código JS está incrustado en la vista), se espera tener la inicialización de los manejadores de eventos correctamente modularizado, tal [como se muestra en este codepen](https://codepen.io/hcadavid/pen/BwWbrw).

![image](https://github.com/user-attachments/assets/b762f819-a527-4e49-b77a-fbc6300b61b1)

2. Agregue lo que haga falta en sus módulos para que cuando se capturen nuevos puntos en el canvas abierto (si no se ha seleccionado un canvas NO se debe hacer nada):
	1. Se agregue el punto al final de la secuencia de puntos del canvas actual (sólo en la memoria de la aplicación, AÚN NO EN EL API!).
	2. Se repinte el dibujo.

![image](https://github.com/user-attachments/assets/09baf49f-72c9-4849-873d-7a239ac590db)

3. Agregue el botón Save/Update. Respetando la arquitectura de módulos actual del cliente, haga que al oprimirse el botón:
	1. Se haga PUT al API, con el plano actualizado, en su recurso REST correspondiente.
	2. Se haga GET al recurso /blueprints, para obtener de nuevo todos los planos realizados.
	3. Se calculen nuevamente los puntos totales del usuario.

	```
 	function saveOrUpdateBlueprint() {
        if (!newBlueprintInProgress) {
            var updatedBlueprint = {
                author: author,
                points: currentCanvasPoints,
                name: decodeURIComponent(blueprintName)
            };
            apiFunction.updateBlueprint(author, blueprintName, updatedBlueprint)
                .then(function () {
                    return apiFunction.getBlueprintsByAuthor(author);
                })
                .then(function (newBlueprints) {
                    blueprints = newBlueprints.map(function (blueprint) {
                        return { name: blueprint.name, points: blueprint.points };
                    })
                    updateBlueprintTable();
                    alert("Plano actualizado con éxito");
                })
                .catch(function (error) {
                    alert("Error al actualizar el plano.");
                });
        }else{
            if (currentCanvasPoints.length == 0) {
                alert("The plane must have at least one point before saving.");
                return;
            }
            var newBlueprint = {
                author: author,
                points: currentCanvasPoints,
                name: blueprintName
            };
            console.log(newBlueprint);
            apiFunction.createBlueprint(newBlueprint)
                .then(function () {
                    return apiFunction.getBlueprintsByAuthor(author);
                })
                .then(function (newBlueprints) {
                    blueprints = newBlueprints.map(function (blueprint) {
                        return { name: blueprint.name, points: blueprint.points };
                    });
                    clear();
                    updateBlueprintTable();
                    alert("Plano agregado con éxito");
                })
                .catch(function (error) {
                    alert("Error al Agregar el plano.");
                });

        }
    }
 	```
	Para lo anterior tenga en cuenta:

	* jQuery no tiene funciones para peticiones PUT o DELETE, por lo que es necesario 'configurarlas' manualmente a través de su API para AJAX. Por ejemplo, para hacer una peticion PUT a un recurso /myrecurso:

	```javascript
    return $.ajax({
        url: "/mirecurso",
        type: 'PUT',
        data: '{"prop1":1000,"prop2":"papas"}',
        contentType: "application/json"
    });

 
	```
 	![image](https://github.com/user-attachments/assets/1f28ad9c-a13e-43f3-8cbd-3d96a76e1610)

	Para éste note que la propiedad 'data' del objeto enviado a $.ajax debe ser un objeto jSON (en formato de texto). Si el dato que quiere enviar es un objeto JavaScript, puede convertirlo a jSON con: 
	
	```javascript
	JSON.stringify(objetojavascript),
	```
	* Como en este caso se tienen tres operaciones basadas en _callbacks_, y que las mismas requieren realizarse en un orden específico, tenga en cuenta cómo usar las promesas de JavaScript [mediante alguno de los ejemplos disponibles](http://codepen.io/hcadavid/pen/jrwdgK).

4. Agregue el botón 'Create new blueprint', de manera que cuando se oprima: 
	* Se borre el canvas actual.
	* Se solicite el nombre del nuevo 'blueprint' (usted decide la manera de hacerlo).
	
	Esta opción debe cambiar la manera como funciona la opción 'save/update', pues en este caso, al oprimirse la primera vez debe (igualmente, usando promesas):

	1. Hacer POST al recurso /blueprints, para crear el nuevo plano.
	2. Hacer GET a este mismo recurso, para actualizar el listado de planos y el puntaje del usuario.

 	En apiclient:
	  ![image](https://github.com/user-attachments/assets/1040bc77-14e7-4989-9b27-09f69f61f845)

    	![image](https://github.com/user-attachments/assets/70209c70-8f26-4716-9075-febc2eb423c2)


6. Agregue el botón 'DELETE', de manera que (también con promesas):
	* Borre el canvas.
	* Haga DELETE del recurso correspondiente.
	* Haga GET de los planos ahora disponibles.

 	![image](https://github.com/user-attachments/assets/6fb27a8d-8211-4d1d-9210-aba501361df2)

 	En apiclient:

 	![image](https://github.com/user-attachments/assets/6c2323d2-1230-4384-b7b5-f57c965f10a9)


EJEMPLO DE FUNCIONAMIENTO:

Antes de Actualizar:

![image](https://github.com/user-attachments/assets/ee526b7e-0fbe-4392-ba2a-893184c29e33)

Despues de Actualizar:

![image](https://github.com/user-attachments/assets/c64e31d7-fbb8-4dab-a453-64c392ae9a0a)




### Criterios de evaluación

1. Funcional
	* La aplicación carga y dibuja correctamente los planos.
	* La aplicación actualiza la lista de planos cuando se crea y almacena (a través del API) uno nuevo.
	* La aplicación permite modificar planos existentes.
	* La aplicación calcula correctamente los puntos totales.
2. Diseño
	* Los callback usados al momento de cargar los planos y calcular los puntos de un autor NO hace uso de ciclos, sino de operaciones map/reduce.
	* Las operaciones de actualización y borrado hacen uso de promesas para garantizar que el cálculo del puntaje se realice sólo hasta cando se hayan actualizados los datos en el backend. Si se usan callbacks anidados se evalúa como R.
	
