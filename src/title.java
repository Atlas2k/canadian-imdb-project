public class title {
    String tconst;
    String titleType;
    String primaryTitle;
    String originalTitle;
    String isAdult;
    String startYear;
    String endYear;
    String runtimeMinutes;
    String genres;

    public String getTconst() {
        return tconst;
    }

    public String getTitleType() {
        return titleType;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getIsAdult() {
        return isAdult;
    }

    public String getStartYear() {
        return startYear;
    }

    public String  getEndYear() {
        return endYear;
    }

    public String getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public String getGenres() {
        return genres;
    }

    public title(String tconst,
                 String titleType,
                 String primaryTitle,
                 String originalTitle,
                 String isAdult,
                 String startYear,
                 String endYear,
                 String runtimeMinutes,
                 String genres){
        this.tconst = tconst;
        this.titleType = titleType;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.isAdult = isAdult;
        this.startYear = startYear;
        this.endYear = endYear;
        this.runtimeMinutes = runtimeMinutes;
        this.genres = genres;



    }
}
