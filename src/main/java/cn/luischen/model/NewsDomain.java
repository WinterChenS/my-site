package cn.luischen.model;


/**
 * 新闻类
 */
public class NewsDomain {

    private Integer id;

    private String title;

    private String Content;

    private long createAt;

    public NewsDomain() {
    }

    public NewsDomain(Integer id, String title, String content, long createAt) {
        this.id = id;
        this.title = title;
        Content = content;
        this.createAt = createAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "NewsDomain{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", Content='" + Content + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
