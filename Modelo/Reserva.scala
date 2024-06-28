package Modelo
import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success, Try}
import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}
import scala.collection.mutable

class Reserva(archivoReservasAprobadas: String = "reservas_aprobadas.dat",
              archivoReservasPendientes: String = "reservas_pendientes.dat") {

  private var reservasAprobadas: ArrayBuffer[mutable.Map[String, String]] = ArrayBuffer()
  private var reservasPendientes: ArrayBuffer[mutable.Map[String, String]] = ArrayBuffer()
  private var reservasRechazadas:ArrayBuffer[mutable.Map[String, String]] = ArrayBuffer()
  private val archivoReservasAprobadasPath: String = archivoReservasAprobadas
  private val archivoReservasPendientesPath: String = archivoReservasPendientes
  cargarReservasAprobadas()
  cargarReservasPendientes()

    def hacerReserva(laboratorio: String, fecha: String, hora: String, estado: String): Unit = {
      val reserva = mutable.Map(
        "laboratorio" -> laboratorio,
        "fecha" -> fecha,
        "hora" -> hora,
        "pendiente" -> estado
      )
      reservasPendientes += reserva
      guardarReservasPendientes()
    }

  def guardarReservasPendientes(): Unit = {
    val data: Map[String, ArrayBuffer[mutable.Map[String, String]]] = Map("reservasPendientes" -> reservasPendientes)
    val fileOut = new ObjectOutputStream(new FileOutputStream(archivoReservasPendientesPath))
    fileOut.writeObject(data)
    fileOut.close()
  }

  def cargarReservasPendientes(): Unit = {
    Try {
      val fileIn = new ObjectInputStream(new FileInputStream(archivoReservasPendientesPath))
      val data = fileIn.readObject().asInstanceOf[Map[String, ArrayBuffer[mutable.Map[String, String]]]]
      fileIn.close()

      if (data.contains("reservasPendientes") && data("reservasPendientes").isInstanceOf[ArrayBuffer[mutable.Map[String, String]]]) {
        reservasPendientes = data("reservasPendientes").asInstanceOf[ArrayBuffer[mutable.Map[String, String]]]

        reservasPendientes.foreach(reserva => {
          if (reserva.contains("pendiente")) {
            reserva.put("estado", reserva("pendiente"))

          }
        })
      }
    } match {
      case Success(_) =>
      case Failure(e) => println(s"Error al cargar reservas pendientes: $e")
    }
  }


  def obtenerReservasPendientes(): ArrayBuffer[mutable.Map[String, String]] = {
    reservasPendientes
  }

  def aprobarReservas(): Unit = {
    val indicesAprobar = reservasPendientes.zipWithIndex.filter { case (reserva, _) =>
      reserva("estado") == "pendiente"
    }.map(_._2)
    indicesAprobar.foreach { indice =>
      if (0 <= indice && indice < reservasPendientes.length) {
        val reserva = reservasPendientes(indice)
        reserva("estado") = "Aprobada"
        reservasAprobadas += reserva
      }
    }
    reservasPendientes --= indicesAprobar.map(reservasPendientes)
    guardarReservasPendientes()
    guardarReservasAprobadas()
  }

  def guardarReservasAprobadas(): Unit = {
    val data: Map[String, ArrayBuffer[mutable.Map[String, String]]] = Map("reservasAprobadas" -> reservasAprobadas)
    val fileOut = new ObjectOutputStream(new FileOutputStream(archivoReservasAprobadasPath))
    fileOut.writeObject(data)
    fileOut.close()
  }


  def cargarReservasAprobadas(): Unit = {
    Try {
      val fileIn = new ObjectInputStream(new FileInputStream(archivoReservasAprobadasPath))
      val data = fileIn.readObject().asInstanceOf[Map[String, ArrayBuffer[mutable.Map[String, String]]]]
      fileIn.close()
      if (data.contains("reservasAprobadas") && data("reservasAprobadas").isInstanceOf[ArrayBuffer[mutable.Map[String, String]]]) {
        reservasAprobadas = data("reservasAprobadas").asInstanceOf[ArrayBuffer[mutable.Map[String, String]]]
      }
    } match {
      case Success(_) =>
      case Failure(e) => println(s"Error al cargar reservas aprobadas: $e")
    }
  }

  def obtenerReservasAprobadas(): ArrayBuffer[mutable.Map[String, String]] = {
    reservasAprobadas
  }




  def rechazarReserva(): Unit = {
    val indicesRechazar = reservasPendientes.zipWithIndex.filter { case (reserva, _) =>
      reserva("estado") == "pendiente"
    }.map(_._2)
    indicesRechazar.foreach { indice =>
      if (0 <= indice && indice < reservasPendientes.length) {
        val reserva = reservasPendientes(indice)
        reserva("estado") = "Rechazada"
        reservasRechazadas += reserva
        reservasPendientes.remove(indice)
      }
    }
    guardarReservasPendientes()
  }

}
