package Controlador

import Modelo.{Laboratorio, Reserva, Usuario}
import Vista.{ventana_principal, ver_labmasreservado}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.table.DefaultTableModel

class Cntrl_labmasreservado(ventanaver: ver_labmasreservado, usuarioModelo: Usuario) {
  var ventanaPrincipal = new ventana_principal()

  def Iniciar(): Unit = {
    val laboratorioModelo = new Laboratorio
    val reservaModelo = new Reserva()
    ventanaver.setTitle("Ver laboratorios reservados")
    ventanaver.setLocationRelativeTo(null)
    ventanaver.setVisible(true)
    ventanaver.setResizable(false)
    ventanaver.btn_aprobar.setVisible(false)
    ventanaver.btn_rechazar.setVisible(false)
    ventanaver.btn_ver.setVisible(false)
    ventanaver.tabla1.setVisible(true)
    val laboratorios = laboratorioModelo.obtenerLaboratoriss()
    laboratorios.foreach { laboratorio =>
      println(s"Laboratorio: ${laboratorio("codigo")} - ${laboratorio("nombre")}")
    }

    val reservasap = reservaModelo.obtenerReservasAprobadas()
    reservasap.foreach { reserva =>
      if (reserva.contains("estado")) {
        println(s"Reserva Aprobada: Laboratorio-${reserva("laboratorio")}, Fecha-${reserva("fecha")}, Hora-${reserva("hora")}, Estado-${reserva("estado")}")
      } else {
        println(s"Reserva Aprobada sin estado definido")
      }
    }

    val laboratoriosReservados = reservasap.groupBy(_("laboratorio"))

    if (laboratoriosReservados.isEmpty) {
      println("No hay laboratorios reservados")
    } else {
      val laboratorioMasReservado = laboratoriosReservados.maxBy { case (_, reservas) =>
        reservas.length
      }

      val codigoLaboratorio = laboratorioMasReservado._1
      val nombreLaboratorio = laboratorios.find(_("codigo") == codigoLaboratorio).map(_("nombre")).getOrElse("")

      ventanaver.cbox_labs.addItem(s"$codigoLaboratorio - $nombreLaboratorio")

      val reservasLaboratorio = reservasap.filter(_("laboratorio") == codigoLaboratorio)

      val model = new DefaultTableModel()
      model.addColumn("Código ")
      model.addColumn("Nombre ")
      model.addColumn("Fecha")
      model.addColumn("Hora")
      model.addColumn("Estado")
      for (reserva <- reservasLaboratorio) {
        model.addRow(Array[AnyRef](
          codigoLaboratorio,
          nombreLaboratorio,
          reserva("fecha"),
          reserva("hora"),
          reserva("estado")
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
}

