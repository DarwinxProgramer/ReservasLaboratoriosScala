package Controlador
import Modelo.{Laboratorio, Reserva, Usuario}
import Vista.{ventana_principal, ver_laboratorios_reservas}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class Cntrol_verreservas(ventanaver: ver_laboratorios_reservas,usuarioModelo: Usuario) {
  var ventanaPrincipal = new ventana_principal()
  def Iniciar(): Unit = {
    var laboratorioModelo = new Laboratorio
    var reservaModelo = new Reserva()
    ventanaver.setTitle("Ver laboratorios reservados")
    ventanaver.setLocationRelativeTo(null)
    ventanaver.setVisible(true)
    ventanaver.setResizable(false)
    ventanaver.tabla1.setVisible(false)
    ventanaver.btn_aprobar.setVisible(false)
    ventanaver.btn_rechazar.setVisible(false)

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

    laboratorios.foreach(laboratorio => ventanaver.cbox_labs.addItem(s"${laboratorio("codigo")} - ${laboratorio("nombre")}"))

    ventanaver.btn_ver.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        // Obtener el código del laboratorio seleccionado
        val laboratorioSeleccionado = ventanaver.cbox_labs.getSelectedItem.toString.split(" - ")
        val codigoLaboratorio = laboratorioSeleccionado(0)
        val nombreLaboratorio = laboratorioSeleccionado(1)

        if (codigoLaboratorio.isEmpty) {
          JOptionPane.showMessageDialog(ventanaver, "Seleccione un laboratorio", "Error", JOptionPane.ERROR_MESSAGE)
        } else {
          val reservasAprobadas = reservaModelo.obtenerReservasAprobadas()
          val reservasLaboratorio = reservasAprobadas.filter(_("laboratorio") == codigoLaboratorio)
          if (reservasLaboratorio.isEmpty) {
            JOptionPane.showMessageDialog(ventanaver, "No hay reservas aprobadas para el laboratorio seleccionado", "Información", JOptionPane.INFORMATION_MESSAGE)
          } else {
            val model = new DefaultTableModel()
            model.addColumn("Código Lab")
            model.addColumn("Nombre Lab")
            model.addColumn("Fecha")
            model.addColumn("Hora")
            model.addColumn("Estado")
            for (reserva <- reservasLaboratorio) {
              model.addRow(Array[AnyRef](
                codigoLaboratorio.asInstanceOf[AnyRef],
                nombreLaboratorio.asInstanceOf[AnyRef],
                reserva("fecha").asInstanceOf[AnyRef],
                reserva("hora").asInstanceOf[AnyRef],
                reserva("estado").asInstanceOf[AnyRef]
              ))
            }
            ventanaver.tabla1.setModel(model)
            ventanaver.tabla1.setVisible(true)
          }
        }
      }
    })

    ventanaver.btn_regresar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_VentanaPrincipal(ventanaPrincipal, usuarioModelo)

        c.Iniciar()
        ventanaver.dispose()
      }
    })
  }
}
