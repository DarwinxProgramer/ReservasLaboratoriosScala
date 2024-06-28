package Controlador
import Vista.{acercade, ventana_principal}
import Modelo.Usuario
import java.awt.event.{ActionEvent, ActionListener}

class Cntrl_acercade(ventanainformacio: acercade,usuarioModelo: Usuario) {
  var ventanaPrincipal = new ventana_principal()

  def Iniciar(): Unit = {
    ventanainformacio.setTitle("Acerca de")
    ventanainformacio.setLocationRelativeTo(null)
    ventanainformacio.setVisible(true)
    ventanainformacio.setResizable(false)

    ventanainformacio.btn_regresar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_VentanaPrincipal(ventanaPrincipal, usuarioModelo)
        c.Iniciar()
        ventanainformacio.dispose()
      }
    })
  }
}
