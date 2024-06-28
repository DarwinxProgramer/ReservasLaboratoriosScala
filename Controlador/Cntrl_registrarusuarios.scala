package Controlador
import Vista.{usuarios, ventana_principal}

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JOptionPane
import scala.collection.mutable
import Modelo.Usuario

import javax.swing.table.DefaultTableModel

class Cntrl_registrarusuarios(ventanaUsuarios: usuarios, usuarioModelo: Usuario) {
  var ventanaPrincipal = new ventana_principal()

  def Iniciar(): Unit = {
    ventanaUsuarios.setTitle("Registrar Usuarios")
    ventanaUsuarios.setLocationRelativeTo(null)
    ventanaUsuarios.setVisible(true)
    ventanaUsuarios.setResizable(false)
    ventanaUsuarios.tablausuarios.setVisible(false)

    ventanaUsuarios.btn_registrar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val id = ventanaUsuarios.txt_cdl.getText()
        val nombre = ventanaUsuarios.txt_nombres.getText()
        val apellido = ventanaUsuarios.txt_apellidos.getText()
        val rol = ventanaUsuarios.cbox_rol.getSelectedItem.toString
        val email = ventanaUsuarios.txt_correo.getText()
        val contrasena = ventanaUsuarios.txt_contrasena.getText()

        if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || rol.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
          JOptionPane.showMessageDialog(ventanaUsuarios, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE)
        } else if (id.length != 10 || !id.forall(_.isDigit)) {
          JOptionPane.showMessageDialog(ventanaUsuarios, "La cédula ingresada no es válida", "Error", JOptionPane.ERROR_MESSAGE)
        } else if (!nombre.matches("^[a-zA-Z]+$") || !apellido.matches("^[a-zA-Z]+$")) {
          JOptionPane.showMessageDialog(ventanaUsuarios, "El nombre y apellido deben contener solo letras", "Error", JOptionPane.ERROR_MESSAGE)
        } else if (!email.endsWith("@ucuenca.edu.ec")) {
          JOptionPane.showMessageDialog(ventanaUsuarios, "El correo electrónico debe terminar en '@ucuenca.edu.ec'", "Error", JOptionPane.ERROR_MESSAGE)
        } else {
          usuarioModelo.crearUsuario(id, nombre, apellido, rol, email, contrasena)
          JOptionPane.showMessageDialog(ventanaUsuarios, "Usuario registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE)
          actualizarTablaUsuarios()
        }
      }
    })

    ventanaUsuarios.btn_buscar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val idBuscar = ventanaUsuarios.txt_buscar.getText()
        val usuarioEncontrado = usuarioModelo.buscarUsuarioPorId(idBuscar)

        usuarioEncontrado.foreach { usuario =>
          ventanaUsuarios.txt_cdl.setText(usuario("id"))
          ventanaUsuarios.txt_nombres.setText(usuario("nombre"))
          ventanaUsuarios.txt_apellidos.setText(usuario("apellido"))
          ventanaUsuarios.cbox_rol.setSelectedItem(usuario("rol"))
          ventanaUsuarios.txt_correo.setText(usuario("email"))
          ventanaUsuarios.txt_contrasena.setText(usuario("contrasena"))
        }
      }
    })

    ventanaUsuarios.btn_modificar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val idModificar = ventanaUsuarios.txt_buscar.getText()
        val usuarioEncontrado = usuarioModelo.buscarUsuarioPorId(idModificar)

        usuarioEncontrado.foreach { usuario =>
          val nuevoUsuario = mutable.Map(
            "id" -> idModificar,
            "nombre" -> ventanaUsuarios.txt_nombres.getText(),
            "apellido" -> ventanaUsuarios.txt_apellidos.getText(),
            "rol" -> ventanaUsuarios.cbox_rol.getSelectedItem().toString(),
            "email" -> ventanaUsuarios.txt_correo.getText(),
            "contrasena" -> ventanaUsuarios.txt_contrasena.getText()
          )
          if (usuarioModelo.modificarUsuario(idModificar, nuevoUsuario)) {
            JOptionPane.showMessageDialog(ventanaUsuarios, "Usuario modificado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE)
            actualizarTablaUsuarios()
          } else {
            JOptionPane.showMessageDialog(ventanaUsuarios, "Error al modificar el usuario", "Error", JOptionPane.ERROR_MESSAGE)
          }

        }
      }
    })

    ventanaUsuarios.btn_eliminar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val idEliminar = ventanaUsuarios.txt_buscar.getText()

        if (usuarioModelo.eliminarUsuario(idEliminar)) {
          JOptionPane.showMessageDialog(ventanaUsuarios, "Usuario eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE)
          ventanaUsuarios.txt_cdl.setText("")
          ventanaUsuarios.txt_nombres.setText("")
          ventanaUsuarios.txt_apellidos.setText("")
          ventanaUsuarios.cbox_rol.setSelectedIndex(0)
          ventanaUsuarios.txt_correo.setText("")
          ventanaUsuarios.txt_contrasena.setText("")
          actualizarTablaUsuarios()
        } else {
          JOptionPane.showMessageDialog(ventanaUsuarios, "Error al eliminar el usuario", "Error", JOptionPane.ERROR_MESSAGE)
        }
      }
    })
    ventanaUsuarios.btn_cancelar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        ventanaUsuarios.txt_cdl.setText("")
        ventanaUsuarios.txt_nombres.setText("")
        ventanaUsuarios.txt_apellidos.setText("")
        ventanaUsuarios.cbox_rol.setSelectedIndex(0)
        ventanaUsuarios.txt_correo.setText("")
        ventanaUsuarios.txt_contrasena.setText("")
      }
    })

    ventanaUsuarios.btn_ver.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val usuarioModelo = new Usuario()
        usuarioModelo.cargarUsuarios()
        val todosLosUsuarios = usuarioModelo.obtenerUsuarios()
        val modeloTabla = new DefaultTableModel()
        modeloTabla.addColumn("ID")
        modeloTabla.addColumn("Nombre")
        modeloTabla.addColumn("Apellido")
        modeloTabla.addColumn("Rol")
        modeloTabla.addColumn("Email")

        for ((userId, usuario) <- todosLosUsuarios) {
          modeloTabla.addRow(Array[AnyRef](
            userId.asInstanceOf[AnyRef],
            usuario("nombre").asInstanceOf[AnyRef],
            usuario("apellido").asInstanceOf[AnyRef],
            usuario("rol").asInstanceOf[AnyRef],
            usuario("email").asInstanceOf[AnyRef]
          ))
        }

        // Asignar el modelo actualizado a tu tabla de usuarios
        ventanaUsuarios.tablausuarios.setModel(modeloTabla)

        // Asegúrate de que la tabla sea visible
        ventanaUsuarios.tablausuarios.setVisible(true)
      }
    })
    ventanaUsuarios.btn_regresar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_VentanaPrincipal(ventanaPrincipal, usuarioModelo)
        c.Iniciar()
        ventanaUsuarios.dispose()
      }
    })
  }

  private def actualizarTablaUsuarios(): Unit = {
    val usuarioModelo = new Usuario()
    usuarioModelo.cargarUsuarios()
    val todosLosUsuarios = usuarioModelo.obtenerUsuarios()
    val modeloTabla = new DefaultTableModel()
    modeloTabla.addColumn("ID")
    modeloTabla.addColumn("Nombre")
    modeloTabla.addColumn("Apellido")
    modeloTabla.addColumn("Rol")
    modeloTabla.addColumn("Email")

    for ((userId, usuario) <- todosLosUsuarios) {
      modeloTabla.addRow(Array[AnyRef](
        userId.asInstanceOf[AnyRef],
        usuario("nombre").asInstanceOf[AnyRef],
        usuario("apellido").asInstanceOf[AnyRef],
        usuario("rol").asInstanceOf[AnyRef],
        usuario("email").asInstanceOf[AnyRef]
      ))
    }

    // Asignar el modelo actualizado a tu tabla de usuarios
    ventanaUsuarios.tablausuarios.setModel(modeloTabla)

    // Asegúrate de que la tabla sea visible
    ventanaUsuarios.tablausuarios.setVisible(true)
  }
}
