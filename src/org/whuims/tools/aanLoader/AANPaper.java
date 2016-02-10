package org.whuims.tools.aanLoader;

public class AANPaper {
    String id = "";
    String title = "";
    String author = "";
    String venue = "";
    String year = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if(title.length()>"La transcription phonétique au bout des doigts, claviers et polices ergonomiques pour la transcription en API (Phonetic transcription at fingertips, ergonomics keyboards and fonts) [in French]".length()){
            title=title.substring(0,"La transcription phonétique au bout des doigts, claviers et polices ergonomiques pour la transcription en API (Phonetic transcription at fingertips, ergonomics keyboards and fonts) [in French]".length());
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        if (author.length() > 120) {
            System.out.println(author.length());
            author = author.substring(0, 120);
        }
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
