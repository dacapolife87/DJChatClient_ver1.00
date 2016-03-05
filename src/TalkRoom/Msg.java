package TalkRoom;

import javax.swing.ImageIcon;

public class Msg {
    private String name;
    private String text;
    private String iconImg;
 
    public Msg(String name, String text) {
        super();
        this.name = name;
        this.text = text;
    }
 
    public String getIcon(){
    	return iconImg;
    }
    public void setIcon(String iconImg){
    	this.iconImg = iconImg;
    }
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getText() {
        return text;
    }
 
    public void setText(String text) {
        this.text = text;
    }
    public String toString() {
        return name + "|" + text;
    }
}
