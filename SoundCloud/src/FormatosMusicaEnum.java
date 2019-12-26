public enum FormatosMusicaEnum {
    mp3, wav;

    public static boolean validaFormato(String formato) {
        for(FormatosMusicaEnum f : values()){
            if(f.name().equals(formato)){
                return true;
            }
        }
        return false;
    }
}
