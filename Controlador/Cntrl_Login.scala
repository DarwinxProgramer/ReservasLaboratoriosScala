package Controlador
import Modelo.Usuario
import Vista.{Login, ventana_principal}
import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}
import javax.swing.{JOptionPane, KeyStroke}
class Cntrl_Login {
  var login = new Login()
  var ventanaPrincipal = new ventana_principal()
  var usuarioModelo = new Usuario()

  def Iniciar(): Unit = {
    login.setTitle("Login")
    login.setLocationRelativeTo(null)
    login.setVisible(true)
    login.setResizable(false)
    usuarioModelo.cargarUsuarios()
    usuarioModelo.obtenerUsuarios().foreach { case (id, userData) =>
      println(s"Usuario: $id, Nombre: ${userData("nombre")}, Apellido: ${userData("apellido")}, Rol: ${userData("rol")}, Correo: ${userData("email")}, Contraseña: ${userData("contrasena")}")
    }


    login.btn_login.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val email = login.txt_user.getText()
        val contrasena = new String(login.jPasswordField.getPassword())
        if (usuarioModelo.verificarCredenciales(email, contrasena)) {
          val rolUsuario = usuarioModelo.obtenerRolUsuario(email)
          usuarioModelo.establecerRolUsuarioAutenticado(rolUsuario.getOrElse(""))
          val c = new Cntrl_VentanaPrincipal(ventanaPrincipal, usuarioModelo)
          c.Iniciar()
          login.dispose()
        } else {
          JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Inténtelo nuevamente.", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE)
        }
      }
    })

    login.salir.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        System.exit(0)
      }
    })


  }
}
