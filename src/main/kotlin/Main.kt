import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.dsl.get
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.slf4j.event.Level
import java.util.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        install(DefaultHeaders) { header(HttpHeaders.Server, "Super Windows Server 2077") }
        install(CallLogging) { level = Level.INFO }
        install(ContentNegotiation) { json() }
        install(SwaggerUI) {
            swagger {
                swaggerUrl = "docs"
                forwardRoot = true
            }
            info {
                title = "Example API"
                version = "latest"
                description = "Example API for testing and demonstration purposes."
            }
            server {
                url = "http://localhost:8080"
                description = "Development Server"
            }
        }
        routing()
    }.start(wait = true)
}

fun Application.routing() {
    routing {
        people()
    }
}

fun Routing.people() {
    route("/schedule") {
        get("/group", {
            tags = listOf("Groups methods")
            description = "Get group schedule by id."
            request {
                queryParameter<Int>("group_id") {
                    description = "Group ID"
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "The schedule has been received."
                    body<PerfectModel> {
                        description = "Response."
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "An invalid operation was provided"
                }
            }
        }) {
            when (call.parameters["group_id"]?.toIntOrNull()) {
                1 -> call.respond(
                    HttpStatusCode.OK,
                    PerfectModel(
                        name = "Английский язык",
                        type = "Практическое занятие",
                        number = 1,
                        weekDay = Weekday.MONDAY.russianName,
                        timeFrom = Calendar.getInstance().apply {
                            set(2023, 6, 30, 8, 0, 0)
                        }.time.toString(),
                        timeTo = Calendar.getInstance().apply {
                            set(2023, 6, 30, 9, 45, 0)
                        }.time.toString()
                    )
                )
                else -> call.respond(
                    HttpStatusCode.BadRequest
                )
            }
        }
    }
}

@Serializable
data class PerfectModel(
    val name: String,
    val type: String,
    val number: Int,
    val weekDay: String,
    val timeFrom: String,
    val timeTo: String
)

enum class Weekday(val russianName: String) {
    MONDAY("Понедельник"), TUESDAY("Вторник")
}