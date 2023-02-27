package gq97a6.pjatk.app

import gq97a6.pjatk.app.G.settings
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.system.measureTimeMillis

object Fetcher {
    private const val href = "https://planzajec.pjwstk.edu.pl"

    internal suspend inline fun fetch(
        login: String = settings.login,
        pass: String = settings.pass,
        weeks: Int = settings.weeks,
        eta: Long = 10000,
        onDone: (Pair<List<Course>?, String>) -> Unit = {}
    ) {
        var result: Pair<List<Course>?, String>

        measureTimeMillis {
            result = try {
                Pair(fetch(login, pass, weeks), "Przekroczono czas")
            } catch (e: FetchException) {
                Pair(null, e.message)
            } catch (e: Exception) {
                Pair(null, "Błąd wewnętrzny")
            }
        }.let {
            delay(maxOf(eta - it, 0))
            onDone(result)
        }
    }

    private suspend fun fetch(login: String, pass: String, weeks: Int): List<Course>? =
        withTimeoutOrNull(10000) {
            //Get necessary body values
            val html = Jsoup
                .connect("$href/Logowanie.aspx")
                .method(Connection.Method.GET)
                .execute()
                .parse()

            val eventValidation = html
                .select("input[name=__EVENTVALIDATION]")
                .first()
                ?.attr("value")
                ?: ""

            val viewState = html
                .select("input[name=__VIEWSTATE]")
                .first()
                ?.attr("value")
                ?: ""

            val viewStateGen = html
                .select("input[name=__VIEWSTATEGENERATOR]")
                .first()
                ?.attr("value")
                ?: ""

            //Get cookies
            val cookies = Jsoup.connect("$href/Logowanie.aspx")
                .method(Connection.Method.POST)
                .data("__VIEWSTATE", viewState)
                .data("__VIEWSTATEGENERATOR", viewStateGen)
                .data("__EVENTVALIDATION", eventValidation)
                .data("ctl00\$ContentPlaceHolder1\$Login1\$UserName", login)
                .data("ctl00\$ContentPlaceHolder1\$Login1\$Password", pass)
                .data("ctl00\$ContentPlaceHolder1\$Login1\$LoginButton", "Zaloguj")
                .execute()
                .cookies()

            if (cookies.isEmpty()) throw FetchException("Nieprawidłowe dane logowania")

            getCourses(cookies, weeks).let {
                if (it.isEmpty()) throw FetchException("Nie znaleziono planu")
                else return@withTimeoutOrNull it
            }
        }

    private fun getCourses(cookies: Map<String, String>, weeks: Int): List<Course> {
        val courses: MutableList<Course> = mutableListOf()
        var viewState = ""

        val coursify: (Document) -> Unit = { html ->
            //Get current view state
            viewState = html
                .select("input[name=__VIEWSTATE]")
                .first()!!
                .attr("value")

            html
                .getElementById("ctl00_ContentPlaceHolder1_DedykowanyPlanStudenta_PlanZajecRadScheduler")
                ?.getElementsByClass("rsContentTable")
                ?.first()
                ?.select("[id*=ctl00_ContentPlaceHolder1_DedykowanyPlanStudenta_PlanZajecRadScheduler]")
                ?.map { it.attr("title").dropLast(1) } //Select titles
                ?.map { getDetail(it, viewState, cookies) } //Get details of course
                ?.map { parseDetail(it) } //Parse detial
                ?.let { courses.addAll(it) }
        }

        //Coursify first page
        coursify(Jsoup.connect("$href/TwojPlan.aspx").cookies(cookies).get())

        //Return if one week required
        if (weeks <= 1) return courses

        for (i in 1 until weeks) {
            //Cursify next page
            val next = Jsoup.connect("$href/TwojPlan.aspx")
                .method(Connection.Method.POST)
                .cookies(cookies)
                //.header("X-MicrosoftAjax", "Delta=true")
                .data("__EVENTARGUMENT", "{\"Command\":\"NavigateToNextPeriod\"}")
                .data("__VIEWSTATE", viewState)
                .data(
                    "__EVENTTARGET",
                    "ctl00\$ContentPlaceHolder1\$DedykowanyPlanStudenta\$PlanZajecRadScheduler"
                )
                .data(
                    "ctl00\$RadScriptManager1",
                    "ctl00\$ContentPlaceHolder1\$ctl00\$ContentPlaceHolder1\$DedykowanyPlanStudenta\$AjaxPanel1Panel|ctl00\$ContentPlaceHolder1\$DedykowanyPlanStudenta\$PlanZajecRadScheduler"
                )
                .execute()

            coursify(next.parse())
        }

        return courses
    }

    private fun getDetail(
        title: String,
        viewState: String,
        cookies: Map<String, String>
    ): Document =
        Jsoup.connect("$href/TwojPlan.aspx")
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
                "{\"AjaxTargetControl\":\"\",\"Value\":\"$title\"}"
            )
            .execute()
            .parse()

    private fun parseDetail(d: Document): Course {
        val get: (String, String) -> String = { id, alt ->
            val value = d.getElementById("ContentPlaceHolder1_DedykowanyPlanStudenta_ctl10_$id")
                ?: d.getElementById("ContentPlaceHolder1_DedykowanyPlanStudenta_ctl10_$alt")
                ?: Element("err")

            value.text().trim()
        }

        return Course(
            get("NazwaPrzedmiotyLabel", "NazwyPrzedmiotowLabel"),
            get("KodPrzedmiotuLabel", "KodyPrzedmiotowLabel"),
            get("TypZajecLabel", "TypRezerwacjiLabel"),
            get("GrupyLabel", "GrupyStudenckieLabel"),
            get("DydaktycyLabel", "OsobaRezerwujacaLabel").split(", "),
            get("BudynekLabel", ""),
            get("SalaLabel", ""),
            get("DataZajecLabel", ""),
            get("GodzRozpLabel", ""),
            get("GodzZakonLabel", ""),
            get("CzasTrwaniaLabel", ""),
            get("KodMsTeamsLabel", "")
        )
    }

    class FetchException(override val message: String) : Exception(message)
}