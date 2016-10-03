package demo.cardviewphpmysql;

class Person {
    String id;
    String descrip;
    //int photoId;



    Person(String id, String descrip) {

        this.id = id;
        this.descrip = descrip;
        //this.photoId = photoId;
    }

    public String getId() {
        return id;
    }

    public String getDescrip() {
        return descrip;
    }
}