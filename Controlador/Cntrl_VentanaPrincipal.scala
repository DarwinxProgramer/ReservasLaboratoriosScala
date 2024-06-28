package Controlador

import Modelo.Usuario
import Vista.{acercade, laboratorios, realizarreservas, usuarios, ventana_principal, ver_labmasreservado, ver_laboratorios_reservas, ver_toltaldelabs}

import java.awt.event.{ActionEvent, ActionListener}

class Cntrl_VentanaPrincipal(ventanaPrincipal: ventana_principal, usuarioModelo: Usuario) {
  var ventanaUsuarios = new usuarios()
  var ventanaLaboratorios = new laboratorios()
  var ventanaVerLaboratoriosReservas = new ver_laboratorios_reservas()
  var ventanaAcercaDe = new acercade()
  var ventanaRealizarReservas = new realizarreservas()
  var ventanaTotallaboratoriosReservaoos = new ver_toltaldelabs()
  var ventanalabsmasreservado = new ver_labmasreservado
  def Iniciar(): Unit = {
    ventanaPrincipal.setTitle("Menú Principal")
    ventanaPrincipal.setLocationRelativeTo(null)
    ventanaPrincipal.setVisible(true)
    ventanaPrincipal.setResizable(false)

    val rolUsuario = usuarioModelo.obtenerRolUsuarioAutenticado()
    println(s"Rol del usuario: $rolUsuario")
    if (rolUsuario.contains("admin")) {
      ventanaPrincipal.menu_crear.setVisible(true)
    } else {
      ventanaPrincipal.menu_crear.setVisible(false)
    }


    ventanaPrincipal.menuitem_usuarios.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_registrarusuarios(ventanaUsuarios,usuarioModelo: Usuario)
        c.Iniciar()
        ventanaPrincipal.dispose()
      }
    })

    ventanaPrincipal.menuitem_labs.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_laboratorios(ventanaLaboratorios,usuarioModelo: Usuario)
        c.Iniciar()
        ventanaPrincipal.dispose()
      }
    })

    ventanaPrincipal.menuitem_solicitudes.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_solicitudes(ventanaVerLaboratoriosReservas,usuarioModelo: Usuario)
        c.Iniciar()
        ventanaPrincipal.dispose()
      }
    })

    ventanaPrincipal.menuitem_labs1.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_realizarreservas(ventanaRealizarReservas,usuarioModelo: Usuario)
        c.Iniciar()
        ventanaPrincipal.dispose()
      }
    })

    ventanaPrincipal.menuitem_Hlabs.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrol_verreservas(ventanaVerLaboratoriosReservas, usuarioModelo: Usuario)
        c.Iniciar()
        ventanaPrincipal.dispose()
      }
    })
    ventanaPrincipal.menuitem_labmasreservado.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val C = new Cntrl_labmasreservado(ventanalabsmasreservado,usuarioModelo: Usuario)
        C.Iniciar()
        ventanaPrincipal.dispose()
      }
    })
    ventanaPrincipal.menuitem_totallabsreservados.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val C = new Cntrl_totallabsreservados(ventanaTotallaboratoriosReservaoos,usuarioModelo: Usuario)
        C.Iniciar()
        ventanaPrincipal.dispose()
      }
    })

    ventanaPrincipal.btn_regresar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_Login
        c.Iniciar()
        ventanaPrincipal.dispose()
      }
    })

    ventanaPrincipal.menuitem_acercade.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_acercade(ventanaAcercaDe, usuarioModelo: Usuario)
        c.Iniciar()
        ventanaPrincipal.dispose()
      }
    })

  }
}
