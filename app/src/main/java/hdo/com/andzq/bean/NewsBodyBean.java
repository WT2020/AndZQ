package hdo.com.andzq.bean;

import org.litepal.crud.DataSupport;

public class NewsBodyBean extends DataSupport {
    private String type; //类型
    private int id; //新闻id
    private String contents; //内容
    private String title;   //标题
    private int hits;//点击量
    private String publisher;//发布者
    private String updates;//更新时间
    private String image;//图片
    private String publishDate;//腹部时间
    private String pretext;//预览text

    public String getPretext() {
        return pretext;
    }

    public void setPretext(String pretext) {
        this.pretext = pretext;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUpdates() {
        return updates;
    }

    public void setUpdates(String updates) {
        this.updates = updates;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}