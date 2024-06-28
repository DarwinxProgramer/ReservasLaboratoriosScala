package Controlador

import Modelo.{Laboratorio, Reserva, Usuario}
import Vista.{ventana_principal, ver_toltaldelabs}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.table.DefaultTableModel

class Cntrl_totallabsreservados(ventanaver: ver_toltaldelabs,usuarioModelo: Usuario) {
  var ventanaPrincipal = new ventana_principal()
  var laboratorioModelo = new Laboratorio
  var reservaModelo = new Reserva()

  def Iniciar(): Unit = {
    ventanaver.setTitle("Ver laboratorios reservados")
    ventanaver.setLocationRelativeTo(null)
    ventanaver.setVisible(true)
    ventanaver.setResizable(false)
    ventanaver.btn_aprobar.setVisible(false)
    ventanaver.btn_rechazar.setVisible(false)
    ventanaver.btn_ver.setVisible(false)
    ventanaver.cbox_labs.setVisible(false)
    val laboratorios = laboratorioModelo.obtenerLaboratoriss()
    val reservasAprobadas = reservaModelo.obtenerReservasAprobadas()
    val model = new DefaultTableModel()
    model.addColumn("Código Lab")
    model.addColumn("Nombre Lab")
    model.addColumn("Reservado")
    for (laboratorio <- laboratorios) {
      val codigoLaboratorio = laboratorio("codigo").toString
      val nombreLaboratorio = laboratorio("nombre").toString
      val reservado = if (reservasAprobadas.exists(_("laboratorio") == codigoLaboratorio)) "Sí" else "No"

      model.addRow(Array[AnyRef](
        codigoLaboratorio.asInstanceOf[AnyRef],
        nombreLaboratorio.asInstanceOf[AnyRef],
        reservado.asInstanceOf[AnyRef]
      ))
    }

    ventanaver.tabla1.setModel(model)
    ventanaver.tabla1.setVisible(true)
  }

  ventanaver.btn_regresar.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      val c = new Cntrl_VentanaPrincipal(ventanaPrincipal, usuarioModelo)
      c.Iniciar()
      ventanaver.dispose()
    }
  })
}

