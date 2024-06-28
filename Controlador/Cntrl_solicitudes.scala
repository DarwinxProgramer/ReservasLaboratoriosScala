package Controlador
import Vista.{ventana_principal, ver_laboratorios_reservas}
import Modelo.Laboratorio
import Modelo.Reserva
import Modelo.Usuario
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel
class Cntrl_solicitudes  (ventanasolicitudes: ver_laboratorios_reservas,usuarioModelo: Usuario) {
  var ventanaPrincipal = new ventana_principal()
  var laboratorioModelo = new Laboratorio
  var reservaModelo = new Reserva()

  def Iniciar(): Unit = {
    ventanasolicitudes.setTitle("Solicitudes de reservaciones")
    ventanasolicitudes.setLocationRelativeTo(null)
    ventanasolicitudes.setVisible(true)
    ventanasolicitudes.setResizable(false)
    ventanasolicitudes.tabla1.setVisible(false)
    val laboratorios = laboratorioModelo.obtenerLaboratoriss()
    laboratorios.foreach { laboratorio =>
      println(s"Laboratorio: ${laboratorio("codigo")} - ${laboratorio("nombre")}")
    }
    val reservasPendientes = reservaModelo.obtenerReservasPendientes()
    reservasPendientes.foreach { reserva =>
      if (reserva.contains("estado")) {
        println(s"Reserva Pendiente: Laboratorio-${reserva("laboratorio")}, Fecha-${reserva("fecha")}, Hora-${reserva("hora")}, Estado-${reserva("estado")}")
      } else {
        println(s"Reserva Pendiente sin estado definido")
      }
    }
    laboratorios.foreach(laboratorio => ventanasolicitudes.cbox_labs.addItem(s"${laboratorio("codigo")} - ${laboratorio("nombre")}"))

    ventanasolicitudes.btn_ver.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val laboratorioSeleccionado = ventanasolicitudes.cbox_labs.getSelectedItem.toString.split(" - ")
        val codigoLaboratorio = laboratorioSeleccionado(0)
        val nombreLaboratorio = laboratorioSeleccionado(1)
        if (codigoLaboratorio.isEmpty) {
          JOptionPane.showMessageDialog(ventanasolicitudes, "Seleccione un laboratorio", "Error", JOptionPane.ERROR_MESSAGE)
        } else {
          val reservasPendientes = reservaModelo.obtenerReservasPendientes()
          val reservasLaboratorio = reservasPendientes.filter(_("laboratorio") == codigoLaboratorio)
          if (reservasLaboratorio.isEmpty) {
            JOptionPane.showMessageDialog(ventanasolicitudes, "No hay reservas pendientes para el laboratorio seleccionado", "Información", JOptionPane.INFORMATION_MESSAGE)
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
            ventanasolicitudes.tabla1.setModel(model)
            ventanasolicitudes.tabla1.setVisible(true)
          }
        }
      }
    })


    ventanasolicitudes.btn_aprobar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val filaSeleccionada = ventanasolicitudes.tabla1.getSelectedRow()
        if (filaSeleccionada == -1) {
          JOptionPane.showMessageDialog(ventanasolicitudes, "Seleccione una reserva para aprobar", "Error", JOptionPane.ERROR_MESSAGE)

        } else {
          val laboratorioSeleccionado = ventanasolicitudes.cbox_labs.getSelectedItem.toString.split(" - ")(0)
          val fecha = ventanasolicitudes.tabla1.getValueAt(filaSeleccionada, 1).toString
          val hora = ventanasolicitudes.tabla1.getValueAt(filaSeleccionada, 2).toString
          reservaModelo.aprobarReservas()
          val model = ventanasolicitudes.tabla1.getModel.asInstanceOf[DefaultTableModel]
          model.removeRow(filaSeleccionada)
          val reservasap = reservaModelo.obtenerReservasAprobadas()
          reservasap.foreach { reserva =>
            if (reserva.contains("estado")) {
              println(s"Reserva Aprobada: Laboratorio-${reserva("laboratorio")}, Fecha-${reserva("fecha")}, Hora-${reserva("hora")}, Estado-${reserva("estado")}")
            } else {
              println(s"Reserva Pendiente sin estado definido")
            }
          }
        }
      }
    })


    ventanasolicitudes.btn_rechazar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val filaSeleccionada = ventanasolicitudes.tabla1.getSelectedRow()
        if (filaSeleccionada == -1) {
          JOptionPane.showMessageDialog(ventanasolicitudes, "Seleccione una reserva para rechazar", "Error", JOptionPane.ERROR_MESSAGE)
        } else {
          val codigoLab = ventanasolicitudes.tabla1.getValueAt(filaSeleccionada, 0).toString
          val fecha = ventanasolicitudes.tabla1.getValueAt(filaSeleccionada, 2).toString
          val hora = ventanasolicitudes.tabla1.getValueAt(filaSeleccionada, 3).toString
          reservaModelo.rechazarReserva()
          val model = ventanasolicitudes.tabla1.getModel.asInstanceOf[DefaultTableModel]
          model.removeRow(filaSeleccionada)
        }
      }
    })


    ventanasolicitudes.btn_regresar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_VentanaPrincipal(ventanaPrincipal, usuarioModelo)
        c.Iniciar()
        ventanasolicitudes.dispose()
      }
    })
  }
}


