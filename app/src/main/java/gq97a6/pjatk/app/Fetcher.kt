package gq97a6.pjatk.app

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat
import java.util.*

object Fetcher {
    fun fetch(login: String, pass: String): List<Course> {
        //Get headers
        var response = Jsoup
            .connect("https://planzajec.pjwstk.edu.pl/Logowanie.aspx")
            .method(Connection.Method.GET)
            .execute()

        var responseDocument = response.parse()

        val eventValidation = responseDocument
            .select("input[name=__EVENTVALIDATION]")
            .first()
            .attr("value")

        var viewState = responseDocument
            .select("input[name=__VIEWSTATE]")
            .first()
            .attr("value")

        val viewStateGen = responseDocument
            .select("input[name=__VIEWSTATEGENERATOR]")
            .first()
            .attr("value")

        //Get cookies
        response = Jsoup.connect("https://planzajec.pjwstk.edu.pl/Logowanie.aspx")
            .method(Connection.Method.POST)
            .data("__VIEWSTATE", viewState)
            .data("__VIEWSTATEGENERATOR", viewStateGen)
            .data("__EVENTVALIDATION", eventValidation)
            .data("ctl00\$ContentPlaceHolder1\$Login1\$UserName", login)
            .data("ctl00\$ContentPlaceHolder1\$Login1\$Password", pass)
            .data("ctl00\$ContentPlaceHolder1\$Login1\$LoginButton", "Zaloguj")
            .execute()

        val cookies = response.cookies()

        //Get timetable
        val html: Document = Jsoup
            .connect("https://planzajec.pjwstk.edu.pl/TwojPlan.aspx")
            .cookies(cookies)
            .get()

        val titles = html
            .getElementById("ctl00_ContentPlaceHolder1_DedykowanyPlanStudenta_PlanZajecRadScheduler")
            .getElementsByClass("rsContentTable").first()
            .select("[id*=ctl00_ContentPlaceHolder1_DedykowanyPlanStudenta_PlanZajecRadScheduler]")
            .map { it.attr("title").dropLast(1) }

        responseDocument = response.parse()

        viewState = responseDocument
            .select("input[name=__VIEWSTATE]")
            .first()
            .attr("value")

        //Get timetable details
        return titles.map {
            response = Jsoup.connect("https://planzajec.pjwstk.edu.pl/TwojPlan.aspx")
                .method(Connection.Method.POST)
                .cookies(cookies)
                .header("X-MicrosoftAjax", "Delta=true")
                .data("__VIEWSTATE", viewState)
                .data(
                    "ctl00\$RadScriptManager1",
                    "ctl00\$ContentPlaceHolder1\$DedykowanyPlanStudenta\$RadToolTipManager1RTMPanel|ctl00\$ContentPlaceHolder1\$DedykowanyPlanStudenta\$RadToolTipManager1RTMPanel"
                )
                .data(
                    "ctl00_ContentPlaceHolder1_DedykowanyPlanStudenta_RadToolTipManager1_ClientState",
                    "{\"AjaxTargetControl\":\"\",\"Value\":\"$it\"}"
                )
                .execute()

            val id = "ContentPlaceHolder1_DedykowanyPlanStudenta_ctl10_"
            response.parse().let { d ->

                val hf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

                val test = d.getElementById("${id}NazwaPrzedmiotyLabel").text()
                Course(
                    d.getElementById("${id}NazwaPrzedmiotyLabel").text().trim(),
                    d.getElementById("${id}KodPrzedmiotuLabel").text().trim(),
                    d.getElementById("${id}TypZajecLabel").text().trim(),
                    d.getElementById("${id}GrupyLabel").text().trim(),
                    d.getElementById("${id}DydaktycyLabel").text().trim().split(", "),
                    d.getElementById("${id}BudynekLabel").text().trim(),
                    d.getElementById("${id}SalaLabel").text().trim(),
                    df.parse(d.getElementById("${id}DataZajecLabel").text().trim()) ?: Date(),
                    hf.parse(d.getElementById("${id}GodzRozpLabel").text().trim()) ?: Date(),
                    hf.parse(d.getElementById("${id}GodzZakonLabel").text().trim()) ?: Date(),
                    d.getElementById("${id}CzasTrwaniaLabel").text().trim(),
                    d.getElementById("${id}KodMsTeamsLabel").text().trim(),
                )
            }
        }
    }
}