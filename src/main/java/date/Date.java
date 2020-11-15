package date;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class Date implements Comparable<Date>{

    public final static int FORMAT_SHORT = 0;
    public final static int FORMAT_NORMAL = 1;
    public final static int FORMAT_LONG = 2;
    public final static int FORMAT_US = 3;

    private int tag = 0;
    private int monat = 0;
    private int jahr = 0;

    /**
     * Erzeugt eine Datumsinstanz mit dem aktuellen Systemdatum.
     */
    public Date() {
        LocalDate today = LocalDate.now();
        this.tag = today.getDayOfMonth();
        this.monat = today.getMonthValue();
        this.jahr = today.getYear();
    }

    /**
     * Erzeugt eine Datumsinstanz im Format TT.MM.YYYY.
     *
     * @param dateString zu parsender String
     */
    public Date(@NotNull String dateString) {
        String[] einzelneDaten = dateString.split("\\.");
        if (einzelneDaten.length != 3)
            throw new IllegalArgumentException("Ungültiger String");
        if (einzelneDaten[2].length() != 4)
            throw new IllegalArgumentException("Ungültige Länge des Jahres");
        this.jahr = Integer.parseInt(einzelneDaten[2]);
        this.monat = Integer.parseInt(einzelneDaten[1]);
        this.tag = Integer.parseInt(einzelneDaten[0]);
        if (!korrektDatum(this.jahr, this.monat, this.tag))
            throw new IllegalArgumentException("Ungültiges Datum");
    }

    /**
     * Erzeugt eine Datumsinstanz, die t Tage nach dem 1.1.1900 liegt.
     *
     * @param tage die Tage seit dem 1.1.1900; muss >= 0 sein
     */
    public Date(int tage) {
        if (tage < 0)
            throw new IllegalArgumentException("Tage seit dem 1.1.1900 müssen positiv sein: " + tage);
        if (tage > 401767)
            throw new IllegalArgumentException("Tage gehen über das Jahr 3000 hinaus.");

        Date help = new Date(1, 1, 1900);
        help.addiereTage(tage);
        this.jahr = help.jahr;
        this.monat = help.monat;
        this.tag = help.tag;

    }

    /**
     * Erzeugt eine Datumsinstanz mit den gegebenen Werten.
     *
     * @param tag   der Tag 1-31 ( abhaengig vom Monat)
     * @param monat das Monat, 1 - 12
     * @param jahr  das Jahr, 1900 - 3000
     */
    public Date(@JsonProperty("tag") int tag, @JsonProperty("monat") int monat, @JsonProperty("jahr") int jahr) {
        if (jahr > 3000 || jahr < 1900)
            throw new IllegalArgumentException(jahr + " ist ungültig");
        this.jahr = jahr;
        this.setMonat(monat);
        this.setTag(tag);
    }

    /**
     * Liefert die zwischen zwei Daten vergangenen Tage.
     *
     * @param d1 das erste Datum
     * @param d2 das zweite Datum
     * @return Tage zwischen <code>d1</code> und <code>d2</code>;
     * positiv wenn <code>d2</code> nach <code>d1</code> liegt, sonst negativ
     */
    public static int tageZwischen(Date d1, Date d2) {
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
        Date datum = (Date) o;
        return tag == datum.tag &&
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
        switch (this.monat) {
            case 1:
                return "Januar";
            case 2:
                return "Februar";
            case 3:
                return "März";
            case 4:
                return "April";
            case 5:
                return "Mai";
            case 6:
                return "Juni";
            case 7:
                return "Juli";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "Oktober";
            case 11:
                return "November";
            case 12:
                return "Dezember";
        }
        return "";
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
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                maxd = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                maxd = 30;
                break;
            case 2:
                // Überprüfung auf Schaltjahr
                if (isSchaltjahr(jahr)) {
                    maxd = 29;
                } else {
                    maxd = 28;
                }
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
        switch (this.wochentagNummer()) {
            case 0:
                return "Montag";
            case 1:
                return "Dienstag";
            case 2:
                return "Mittwoch";
            case 3:
                return "Donnerstag";
            case 4:
                return "Freitag";
            case 5:
                return "Samstag";
            case 6:
                return "Sonntag";
        }
        return "";
    }

    /**
     * Vergleicht das <code>this</code>-Datum mit dem übergebenen.
     *
     * @param d das Datum, mit dem verglichen wird
     * @return eine negative Zahl, wenn d spaeter liegt, positiv, wenn d frueher l i egt und
     * 0 bei gleichem Datum
     */
    @Override
    public int compareTo(Date d) {
        return Integer.compare(this.tageSeit1900(), d.tageSeit1900());
    }

    /**
     * Liefert eine Stringdarstellung i n der Form <code>tt.mm.jjjj</code>
     *
     * @return Stringdarstellung i n der Form <code>tt.mm.jjjj</code>QA QA
     * @override
     */
    @Override
    public String toString() {
        return String.format("%02d.%02d.%04d", this.tag, this.monat, this.jahr);
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
     */
    @JsonIgnore
    public void setJahr(int jahr) {
        this.jahr = jahr;
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
     * @param tag   Tag des zu überpüfenden Datums.
     * @param monat Monat des zu überpüfenden Datums.
     * @param jahr  Jahr des zu überpüfenden Datums.
     * @return true, wenn das Datum korrekt ist, ansonsten falls.
     */
    public static boolean korrektDatum(int tag, int monat, int jahr) {
        if (jahr < 1900) {
            return false;
        }

        if (monat > 12 || monat < 1) {
            return false;
        }
        // maximale Tage auf die des jeweiligen Monats setzen
        int maxd = maxDaysInMonth(monat, jahr);
        return tag <= maxd;
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
