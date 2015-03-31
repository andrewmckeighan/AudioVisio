package audiovisio.utils;

/**
 * @author Matt Gerst
 */
public class VersionString implements Comparable<VersionString> {
    private String version;

    public VersionString( String version ){
        if (version == null){ throw new IllegalArgumentException("Version can not be null"); }
        if (!version.matches("[0-9]+\\.[0-9]+")){ throw new IllegalArgumentException("Invalid version format"); }
        this.version = version;
    }

    public String getVersion(){
        return this.version;
    }

    @Override
    public int compareTo( VersionString o ){
        if (o == null){ return 1; }
        String[] myParts = this.version.split("\\.");
        String[] oParts = o.version.split("\\.");
        int length = Math.max(myParts.length, oParts.length);
        for (int i = 0; i < length; i++){
            int myPart;
            if (i < myParts.length){
                myPart = Integer.parseInt(myParts[i]);
            } else {
                myPart = 0;
            }

            int oPart;
            if (i < oParts.length){
                oPart = Integer.parseInt(oParts[i]);
            } else {
                oPart = 0;
            }

            if (myPart < oPart){ return -1; }
            if (myPart > oPart){ return 1; }
        }

        return 0;
    }

    @Override
    public boolean equals( Object obj ){
        if (this == obj){ return true; }
        if (obj == null){ return false; }
        if (this.getClass() != obj.getClass()){ return false; }
        return this.version.equals(((VersionString) obj).version);
    }

    @Override
    public String toString(){
        return this.version;
    }
}
