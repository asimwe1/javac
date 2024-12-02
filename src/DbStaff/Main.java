package DbStaff;

public class Main {
    public static void main(String[] args) {
        Province province = new Province();
        province.execute();

        District district = new District();
        district.execute();

        Sector sector = new Sector();
        sector.execute();

        Cell cell = new Cell();
        cell.execute();

        Village village = new Village();
        village.execute();
    }
}
