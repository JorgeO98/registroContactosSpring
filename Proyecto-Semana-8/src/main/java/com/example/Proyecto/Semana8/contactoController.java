package com.example.Proyecto.Semana8;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author jorge
 */
@Controller
public class contactoController {

    @Autowired
    private ContactoRepository contactoRepository;

    /**
     * Maneja las solicitudes GET a la ruta raíz de la aplicación. Obtiene todos
     * los contactos de la base de datos y los agrega al modelo. Luego devuelve
     * la vista "index" que muestra una lista de contactos.
     *
     * @param model El modelo utilizado para pasar datos a la vista.
     * @return El nombre de la vista que se mostrará al usuario ("index").
     */
    @GetMapping("")
    public String index(Model model) {
        // Obtener todos los contactos de la base de datos
        List<Contacto> contactos = contactoRepository.findAll();

        // Agregar la lista de contactos al modelo para que estén disponibles en la vista
        model.addAttribute("contactos", contactos);

        // Devolver el nombre de la vista que se mostrará al usuario
        return "index";
    }

    /**
     * Maneja las solicitudes GET a la ruta "/form" de la aplicación. Prepara un
     * nuevo objeto Contacto y lo agrega al modelo. Luego devuelve la vista
     * "form" para que el usuario pueda ingresar los datos del nuevo contacto.
     *
     * @param model El modelo utilizado para pasar datos a la vista.
     * @return El nombre de la vista que se mostrará al usuario ("form").
     */
    @GetMapping("/form")
    public String nuevo(Model model) {
        // Crear un nuevo objeto Contacto
        Contacto nuevoContacto = new Contacto();

        // Agregar el nuevo contacto al modelo para que esté disponible en la vista
        model.addAttribute("contacto", nuevoContacto);

        // Devolver el nombre de la vista que se mostrará al usuario
        return "form";
    }

    /**
     * Maneja las solicitudes POST a la ruta "/form" de la aplicación. Guarda el
     * nuevo contacto enviado desde el formulario en la base de datos. Luego
     * redirige al usuario a la página principal ("/") para mostrar la lista
     * actualizada de contactos.
     *
     * @param contacto El objeto Contacto enviado desde el formulario para ser
     * guardado.
     * @return Una cadena que representa la URL a la que se redirige el usuario
     * después de guardar el contacto ("/").
     */
    @PostMapping("/form")
    public String nuevo(Contacto contacto) {
        // Guardar el nuevo contacto en la base de datos
        contactoRepository.save(contacto);

        // Redirigir al usuario a la página principal para mostrar la lista actualizada de contactos
        return "redirect:/";
    }

    /**
     * Maneja las solicitudes GET a la ruta "/contacto/{id}" de la aplicación
     * para editar un contacto existente. Busca el contacto en la base de datos
     * utilizando su ID. Si el contacto existe, lo agrega al modelo y retorna la
     * vista del formulario de edición. Si el contacto no existe, redirige al
     * usuario a la página de error.
     *
     * @param id El ID del contacto que se va a editar.
     * @param model El modelo de Spring utilizado para pasar datos a la vista.
     * @return Una cadena que representa la vista a la que se redirige el
     * usuario ("contacto" para el formulario de edición si el contacto existe,
     * "error" si no existe).
     */
    @GetMapping("/contacto/{id}")
    public String editarContacto(@PathVariable("id") Integer id, Model model) {
        // Buscar el contacto en la base de datos utilizando su ID
        Optional<Contacto> optionalContacto = contactoRepository.findById(id);

        // Verificar si el contacto existe
        if (optionalContacto.isPresent()) {
            // Si el contacto existe, agregarlo al modelo y retornar la vista del formulario de edición
            Contacto contacto = optionalContacto.get();
            model.addAttribute("contacto", contacto);
            return "contacto"; // Vista del formulario de edición
        } else {
            // Si el contacto no existe, redirigir al usuario a la página de error
            return "error";
        }
    }

    /**
     * Maneja las solicitudes POST a la ruta "/contacto" de la aplicación para
     * actualizar un contacto existente. Actualiza los datos del contacto en la
     * base de datos con la información proporcionada en el formulario. Si el ID
     * del contacto recibido en el formulario es válido y corresponde a un
     * contacto existente, actualiza los datos del contacto y redirige a la
     * página principal. Si el ID es nulo o no corresponde a ningún contacto
     * existente, redirige al usuario a la página de error.
     *
     * @param contacto El objeto Contacto que contiene los datos actualizados
     * del contacto.
     * @return Una cadena que representa la vista a la que se redirige el
     * usuario ("redirect:/" para la página principal si se actualiza el
     * contacto correctamente, "error" si el ID es nulo o no corresponde a
     * ningún contacto existente).
     */
    @PostMapping("/contacto")
    public String actualizarContacto(@ModelAttribute Contacto contacto) {
        // Obtener el ID del contacto desde el objeto recibido en el formulario
        Integer id = contacto.getId();

        // Verificar si el ID es válido
        if (id != null) {
            // Buscar el contacto en la base de datos utilizando el ID
            Optional<Contacto> optionalContacto = contactoRepository.findById(id);

            if (optionalContacto.isPresent()) {
                // Si el contacto existe, obtenerlo de la base de datos
                Contacto contactoExistente = optionalContacto.get();

                // Actualizar los datos del contacto existente con la información del formulario
                contactoExistente.setNombre(contacto.getNombre());
                // Actualiza otros campos según sea necesario

                // Guardar el contacto actualizado en la base de datos
                contactoRepository.save(contactoExistente);

                // Redirigir a la página principal
                return "redirect:/";
            } else {
                // Manejar el caso en que el ID no corresponde a ningún contacto existente
                return "error";
            }
        } else {
            // Manejar el caso en que el ID sea nulo
            return "error";
        }
    }

    /**
     * Maneja las solicitudes GET a la ruta "/eliminar/{id}" de la aplicación
     * para eliminar un contacto existente. Busca el contacto en la base de
     * datos utilizando su ID. Si el contacto existe, lo elimina de la base de
     * datos y redirige al usuario a la página principal. Si el contacto no
     * existe, redirige al usuario a la página de error.
     *
     * @param id El ID del contacto que se va a eliminar.
     * @param model El modelo de Spring utilizado para pasar datos a la vista.
     * @return Una cadena que representa la vista a la que se redirige el
     * usuario ("redirect:/" para la página principal si el contacto se elimina
     * correctamente, "error" si el contacto no existe).
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarContacto(@PathVariable("id") Integer id, Model model) {
        // Buscar el contacto en la base de datos utilizando su ID
        Optional<Contacto> optionalContacto = contactoRepository.findById(id);

        // Verificar si el contacto existe
        if (optionalContacto.isPresent()) {
            // Si el contacto existe, eliminarlo de la base de datos
            contactoRepository.deleteById(id);
            // Redirigir al usuario a la página principal
            return "redirect:/";
        } else {
            // Si el contacto no existe, redirigir al usuario a la página de error
            return "error";
        }
    }

}
