package date;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class DateTime implements Comparable<DateTime> {

    public final static int FORMAT_SHORT = 0;
    public final static int FORMAT_NORMAL = 1;
    public final static int FORMAT_LONG = 2;
    public final static int FORMAT_US = 3;

    private int minuten = 0;
    private int stunden = 0;

    private int tag = 0;
    private int monat = 0;
    private int jahr = 0;

    /**
     * Erzeugt eine Datumsinstanz mit dem aktuellen Systemdatum.
     */
    public DateTime() {
        LocalDateTime today = LocalDateTime.now();

        this.minuten = today.getMinute();

        this.tag = today.getDayOfMonth();
        this.monat = today.getMonthValue();
        this.jahr = today.getYear();

    }

    /**
     * Erzeugt eine Datumsinstanz im Format "TT-MM-YYYY hh:mm:ss" .
     *
     * @param dateString zu parsender String
     */
    public DateTime(@NotNull String dateString) throws NumberFormatException {

        String[] data = dateString.split("\\s+");

        String[] dateDaten = data[0].split("-");
        String[] timeDaten = data[1].split(":");


        if (dateDaten.length != 3 || timeDaten.length != 3)
            throw new IllegalArgumentException("Ungültiger String");
        if (dateDaten[2].length() != 4)
            throw new IllegalArgumentException("Ungültige Länge des Jahres");

        this.jahr = Integer.parseInt(dateDaten[2]);
        this.monat = Integer.parseInt(dateDaten[1]);
        this.tag = Integer.parseInt(dateDaten[0]);

        this.stunden = Integer.parseInt(timeDaten[2]);
        this.minuten = Integer.parseInt(timeDaten[1]);
        if (!korrektDatum(this.minuten, this.stunden, this.tag, this.monat, this.jahr))
            throw new IllegalArgumentException("Ungültiges Datum");
    }

    public static DateTime ofGUIString(String textFromGui){
        String[] splitted = textFromGui.split("\\s+");
        if(splitted.length != 2){
            throw new IllegalArgumentException();
        }

        String[] splittedDate = splitted[0].split("-");
        if(splittedDate.length != 3){
            throw new IllegalArgumentException("date");
        }

        String[] splittedTime = splitted[1].split(":");
        if(splittedTime.length != 2){
            throw new IllegalArgumentException("time");
        }

        try{
            return new DateTime(Integer.parseInt(splittedDate[2]),
                    Integer.parseInt(splittedDate[1]),
                    Integer.parseInt(splittedDate[0]),
                    Integer.parseInt(splittedTime[0]),
                    Integer.parseInt(splittedTime[1])
            );
        } catch(NumberFormatException e){
            throw new IllegalArgumentException("number");
        }
    }

    /**
     * Erzeugt eine Datumsinstanz, die t Tage nach dem 1.1.1900 liegt.
     *
     * @param tage die Tage seit dem 1.1.1900; muss >= 0 sein
     */
    public DateTime(int tage) {
        if (tage < 0)
            throw new IllegalArgumentException("Tage seit dem 1.1.1900 müssen positiv sein: " + tage);
        if (tage > 401767)
            throw new IllegalArgumentException("Tage gehen über das Jahr 3000 hinaus.");

        DateTime help = new DateTime(1, 1, 1900);
        help.addiereTage(tage);
        this.jahr = help.jahr;
        this.monat = help.monat;
        this.tag = help.tag;

        //Uhrzeit standartmaessig auf 0
    }

    /**
     * Erzeugt eine Datumsinstanz mit den gegebenen Werten.
     *
     * @param tag   der Tag 1-31 ( abhaengig vom Monat)
     * @param monat das Monat, 1 - 12
     * @param jahr  das Jahr, 1900 - 3000
     */
    public DateTime(int tag, int monat, int jahr) {
        this(tag, monat, jahr, 0, 0);
    }

    public DateTime(@JsonProperty("tag") int tag,
                    @JsonProperty("monat") int monat,
                    @JsonProperty("jahr") int jahr,
                    @JsonProperty("stunden") int stunden,
                    @JsonProperty("minuten") int minuten) {
        this.setTag(tag);
        this.setMonat(monat);
        this.setJahr(jahr);
        this.setMinuten(minuten);
        this.setStunden(stunden);
    }


    /**
     * Liefert die zwischen zwei Daten vergangenen Tage.
     *
     * @param d1 das erste Datum
     * @param d2 das zweite Datum
     * @return Tage zwischen <code>d1</code> und <code>d2</code>;
     * positiv wenn <code>d2</code> nach <code>d1</code> liegt, sonst negativ
     */
    public static int tageZwischen(DateTime d1, DateTime d2) {
        return d2.tageSeit1900() - d1.tageSeit1900();
    }

    /**
     * Prüft auf Schaltjahr.
     *
     * @param jahr die zu prüfende Jahreszahl
     * @return <code>true</code>, wenn <code>jahr</code> ein Schaltjahr ist, <code>false</code> sonst
     */
    public static boolean isSchaltjahr(int jahr) {
        return (jahr % 4 == 0 && jahr % 100 != 0) || jahr % 400 == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DateTime datum = (DateTime) o;
        return  minuten == datum.minuten &&
                stunden == datum.minuten &&
                tag == datum.tag &&
                monat == datum.monat &&
                jahr == datum.jahr;
    }

    /**
     * Liefert den Namen des Monats.
     *
     * @return den Monatsnamen
     */
    @JsonIgnore
    public String getMonatAsString() {
        return switch (this.monat) {
            case 1 -> "Januar";
            case 2 -> "Februar";
            case 3 -> "März";
            case 4 -> "April";
            case 5 -> "Mai";
            case 6 -> "Juni";
            case 7 -> "Juli";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "Oktober";
            case 11 -> "November";
            case 12 -> "Dezember";
            default -> "";
        };
    }

    /**
     * Erhöht (vermindert) das gespeicherte Datum. Liegt nach dieser
     * Operation das Datum vor dem 1.1.1900,
     * so wird eine IllegalArgumentException geworfen und keine Änderung durchgeführt.
     *
     * @param t die Tage, um die dieses Datum erhöht (t > 0) bzw. vermindert (t < 0) wird
     */
    public void addiereTage(int t) {
        if (t < 0 && -t >= this.tageSeit1900())
            throw new IllegalArgumentException("Ungültige Anzahl der Tage");
        if (t == 0)
            return;

        int tageSeit1900 = this.tageSeit1900() + t;
        int helpJahr = 1900, helpTag = 1, helpMonat = 1;

        while (tageSeit1900 >= (365 + (isSchaltjahr(helpJahr) ? 1 : 0))) {

            tageSeit1900 -= 365 + (isSchaltjahr(helpJahr) ? 1 : 0);
            helpJahr++;
        }

        while (tageSeit1900 >= maxDaysInMonth(helpMonat, helpJahr)) {
            tageSeit1900 -= maxDaysInMonth(helpMonat, helpJahr);
            helpMonat++;
        }

        helpTag = tageSeit1900;
        tageSeit1900 = 0;

        this.tag = helpTag;
        this.monat = helpMonat;
        this.jahr = helpJahr;

    }

    /**
     * Berechnet die max. Anzahl an Tagen in einem Monat.
     *
     * @param month Monat dessen max. Tage berechnet werden.
     * @param jahr  Jahr wird nur übergeben falls es sich um ein Schaltjahr handelt.
     * @return Maximale Anzahl an Tagen in einem bestimmten Monat.
     */
    public static int maxDaysInMonth(int month, int jahr) {
        int maxd = 0;
        switch (month) {
            case 1, 3, 5, 7, 8, 10, 12 -> maxd = 31;
            case 4, 6, 9, 11 -> maxd = 31;
            case 2 -> {
                if (isSchaltjahr(jahr)) {
                    maxd = 29;
                } else {
                    maxd = 28;
                }
            }
            default -> throw new IllegalArgumentException();
        }
        return maxd;
    }

    /**
     * Liefert die Nummer des Wochentages.
     *
     * @return die Nummer des Wochentages im Bereich von 0(Montag) bis 6(Sonntag)
     */
    public int wochentagNummer() {
        //return (this.tag - 5) % 7;
        int a = this.jahr - 1;
        int erg = (a + (int) (a / 4) - (int) (a / 100) + (int) (a / 400) + tageImJahr()) % 7;
        return (erg == 0 ? 6 : erg - 1);
    }

    /**
     * Liefert den Wochentag als String.
     *
     * @return den Wochentag als String
     */
    public String wochentag() {
        return switch (this.wochentagNummer()) {
            case 0 -> "Montag";
            case 1 -> "Dienstag";
            case 2 -> "Mittwoch";
            case 3 -> "Donnerstag";
            case 4 -> "Freitag";
            case 5 -> "Samstag";
            case 6 -> "Sonntag";
            default -> "";
        };
    }

    /**
     * Vergleicht das <code>this</code>-Datum mit dem übergebenen.
     *
     * @param d das Datum, mit dem verglichen wird
     * @return eine negative Zahl, wenn <code>d</code> spaeter liegt, positiv, wenn <code>d</code> frueher liegt und
     * 0 bei gleichem Datum
     */
    @Override
    public int compareTo(DateTime d) {
        if (this.tageSeit1900() != d.tageSeit1900()) {
            return Integer.compare(this.tageSeit1900(), d.tageSeit1900());
        } else if (this.stunden != d.stunden) {
            return Integer.compare(this.stunden, d.stunden);
        } else {
            return Integer.compare(this.minuten, d.minuten);
        }
    }

    /**
     * Liefert eine Stringdarstellung i n der Form <code>tt.mm.jjjj</code>
     *
     * @return Stringdarstellung i n der Form <code>tt.mm.jjjj</code>QA QA
     */
    @Override
    public String toString() {
        return String.format("%02d-%02d-%04d %02d:%02d", this.tag, this.monat, this.jahr, this.stunden, this.minuten);
    }


    /**
     * Liefert eine Stringdarstellung unterschiedlichen Formats
     *
     * @param format Moegliche Werte sind:
     *               <code>Datum.FORMAT_SHORT, Datum.FORMAT_NORMAL, Datum.FORMAT_LONG, Datum.FORMAT_US</code>
     * @return Datum im Format <code>dd.mm.yy</code> bei <code>Datum.FORMAT_SHORT</code>,
     * im Format <code>dd.monat jjjj, wochentag (Monat ausgeschrieben)</code> bei
     * <code>Datum.FORMAT_LONG</code>, im Format <code>jjjj/tt/mm</code> bei
     * <code>Datum.FORMAT_US</code>
     */
    public String toString(int format) {
        if (format == 0) {
            return String.format("%02d.%02d.%02d", this.tag, this.monat, this.jahr % 100);
        }
        if (format == 1) {
            return this.toString();
        }
        if (format == 2) {
            return String.format("%02d.%s %04d, %s", this.tag, this.getMonatAsString(), this.jahr, this.wochentag());
        }
        if (format == 3) {
            return String.format("%04d/%02d/%02d", this.jahr, this.tag, this.monat);
        } else {
            throw new IllegalArgumentException("Ungültiges Format");
        }
    }

    /**
     * Setzt die Instanzvariable <code>monat</code> auf den übergebenen Wert.
     *
     * @param monat Wert auf den die Variable <code>monat</code> gesetzt werden soll.
     * @throws IllegalArgumentException Falls der Parameter ungültig ist.
     */
    @JsonIgnore
    public void setMonat(int monat) throws IllegalArgumentException {
        if (monat > 12 || monat < 1) {
            throw new IllegalArgumentException(monat + " ist kein Gültiger Monat.");
        }
        this.monat = monat;
    }

    /**
     * Setzt die Instanzvariable <code>tag</code> auf den übergebenen Wert.
     *
     * @param tag Wert auf den die Variable <code>tag</code> gesetzt werden soll.
     * @throws IllegalArgumentException Falls der Parameter ungültig ist.
     */
    @JsonIgnore
    public void setTag(int tag) throws IllegalArgumentException {
        switch (this.monat) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (tag > 31 || tag < 1)
                    throw new IllegalArgumentException(tag + " ist kein gültiger Tag.");
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (tag > 30 || tag < 1)
                    throw new IllegalArgumentException(tag + " ist kein gültiger Tag.");
                break;
            case 2:
                if (isSchaltjahr(this.jahr)) {
                    if (tag > 29 || tag < 1)
                        throw new IllegalArgumentException(tag + " ist kein gültiger Tag.");
                } else if (tag > 28 || tag < 1)
                    throw new IllegalArgumentException(tag + "ist kein gültiger Tag.");
        }

        this.tag = tag;
    }

    /**
     * Setzt die Instanzvariable <code>jahr</code> auf den übergebenen Wert.
     *
     * @param jahr Wert auf den die Variable <code>jahr</code> gesetzt werden soll.
     * @throws IllegalArgumentException Falls der Parameter ungültig ist.
     */
    @JsonIgnore
    public void setJahr(int jahr) throws IllegalArgumentException {
        if (jahr > 3000 || jahr < 1900)
            throw new IllegalArgumentException(jahr + " ist kein gültiges Jahr");
        this.jahr = jahr;
    }

    /**
     * Setzt die Instanzvariable <code>minuten</code> auf den übergebenen Wert.
     *
     * @param minuten Wert auf den die Variable <code>minuten</code> gesetzt werden soll.
     * @throws IllegalArgumentException Falls der Parameter ungültig ist.
     */
    @JsonIgnore
    public void setMinuten(int minuten) throws IllegalArgumentException {
        if (minuten < 0 || minuten > 59) {
            throw new IllegalArgumentException(minuten + "ist keine gültige Minutenanzahl");
        }
        this.minuten = minuten;
    }

    /**
     * Setzt die Instanzvariable <code>stunden</code> auf den übergebenen Wert.
     *
     * @param stunden Wert auf den die Variable <code>stunden</code> gesetzt werden soll.
     * @throws IllegalArgumentException Falls der Parameter ungültig ist.
     */
    @JsonIgnore
    public void setStunden(int stunden) throws IllegalArgumentException {
        if (stunden < 0 || stunden > 23) {
            throw new IllegalArgumentException(stunden + "ist keine gültige Stundenanzahl");
        }
        this.stunden = stunden;
    }

    /**
     * Gibt den wert der Instanzvariable <code>jahr</code> zurück.
     *
     * @return Wert der Variable <code>jahr</code>.
     */
    @JsonGetter("jahr")
    public int getJahr() {
        return this.jahr;
    }

    /**
     * Gibt den wert der Instanzvariable <code>monat</code> zurück.
     *
     * @return Wert der Variable <code>monat</code>.
     */
    @JsonGetter("monat")
    public int getMonat() {
        return this.monat;
    }

    /**
     * Gibt den wert der Instanzvariable <code>tag</code> zurück.
     *
     * @return Wert der Variable <code>tag</code>.
     */
    @JsonGetter("tag")
    public int getTag() {
        return this.tag;
    }

    /**
     * Gibt den Wert der Instanzvariable <code>minuten</code> zurück.
     *
     * @return Wert der Variable <code>minuten</code>
     */
    @JsonGetter("minuten")
    public int getMinuten() {
        return minuten;
    }

    /**
     * Gibt den Wert der Instanzvariable <code>stunden</code> zurück.
     *
     * @return Wert der Variable <code>stunden</code>
     */
    @JsonGetter("stunden")
    public int getStunden() {
        return stunden;
    }

    /**
     * Berechnet wie viele Tage im aktuellen Jahr schon vergangen sind. Der aktuelle Tag wird mitberechnet.
     *
     * @return Anzahl der Tage, die bereits in einem Jahr vergangen sind.
     */
    public int tageImJahr() {
        int summe = 0;
        switch (this.monat - 1) {
            case 11:
                summe += 30;
            case 10:
                summe += 31;
            case 9:
                summe += 30;
            case 8:
                summe += 31;
            case 7:
                summe += 31;
            case 6:
                summe += 30;
            case 5:
                summe += 31;
            case 4:
                summe += 30;
            case 3:
                summe += 31;
            case 2:
                summe += 28 + (isSchaltjahr(this.jahr) ? 1 : 0);
            case 1:
                summe += 31;
        }
        return summe + this.tag;
    }

    /**
     * Überprüft ob die übergebenen Daten korrekt ist.
     *
     * @param minuten  Minuten des zu überpüfenden Datums.
     * @param stunden  Stunden des zu überpüfenden Datums.
     * @param tag      Tag des zu überpüfenden Datums.
     * @param monat    Monat des zu überpüfenden Datums.
     * @param jahr     Jahr des zu überpüfenden Datums.
     * @return true, wenn das Datum korrekt ist, ansonsten falls.
     */
    public static boolean korrektDatum(int minuten, int stunden, int tag, int monat, int jahr) {
        if (jahr < 1900) {
            return false;
        } else if (monat > 12 || monat < 1) {
            return false;
        }

        // maximale Tage auf die des jeweiligen Monats setzen
        int maxd = maxDaysInMonth(monat, jahr);


        if (tag > maxd || tag < 0) {
            return false;
        }

        if (stunden >= 24 || stunden < 0) {
            return false;
        }
        return minuten < 60 && minuten >= 1;
    }


    /**
     * Berechnet die Anzahl der Tage seit dem 1.1.1900.
     *
     * @return Tage seit dem 1.1.1900
     */
    public int tageSeit1900() {
        int t = this.tageImJahr();
        if (this.jahr == 1900)
            return t;
        for (int i = 1900; i < this.jahr; i++) {
            t += (isSchaltjahr(i) ? 366 : 365);
        }
        return t;
    }


}
