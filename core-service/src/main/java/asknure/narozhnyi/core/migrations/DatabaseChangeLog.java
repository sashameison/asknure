package asknure.narozhnyi.core.migrations;

import java.util.List;

import asknure.narozhnyi.core.model.Comment;
import asknure.narozhnyi.core.model.Post;
import asknure.narozhnyi.core.model.User;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@ChangeLog(order = "001")
@Profile(value = "dev")
public class DatabaseChangeLog {
  private static final String SASHA = "Sasha";
  private static final String ARTEM = "Artem";

  private static User buildUser(String name, String email) {
    return User.builder()
        .username(name)
        .email(email)
        .postCount(2L)
        .avatar("https://lh3.googleusercontent.com/a-/AOh14Gg0GC72F3UOZUmtULfMxbSPBXPfasnCkIv3oY3w7w=s96-c")
        .build();
  }

  private static Post buildPost(String title) {
    return Post.builder()
        .title(title)
        .color("#3fc4be")
        .categories(List.of())
        .comments(List.of(
            buildComment(SASHA, "Youtube is platform for entertainment"),
            buildComment(ARTEM, "Youtube is big base of knowledge")
        ))
        .build();
  }

  private static Comment buildComment(String username, String text) {
    return Comment.builder()
        .author(username)
        .text(text)
        .build();
  }

  @ChangeSet(order = "001", id = "insertBaseUsers", author = "onarozhnyi")
  public void insertBaseUsers(MongockTemplate mongockTemplate) {
    var sasha = buildUser(SASHA, "sasha2021@gmail.com");
    var artem = buildUser(ARTEM, "Artem2021@gmail.com");

    mongockTemplate.insertAll(List.of(sasha, artem));
  }

  @ChangeSet(order = "002", id = "insertBasePost", author = "onarozhnyi")
  public void insertBasePost(MongockTemplate mongockTemplate) {
    var googlePost = buildPost("How use google productively");
    var youtubePost = buildPost("What is youtube");
    mongockTemplate.insertAll(List.of(googlePost, youtubePost));
  }

  @ChangeSet(order = "003", id = "updateFilesFieldForPosts", author = "onarozhnyi")
  public void updateFilesFieldForPosts(MongockTemplate mongockTemplate) {
    Query query = Query.query(Criteria.where("files").exists(false));
    Update update = new Update();
    update.set("files", List.of());
    mongockTemplate.updateMulti(query, update, Post.class);
  }

  @ChangeSet(order = "004", id = "updateTagsToCategoriesNaming", author = "onarozhnyi")
  public void updateTagsToCategoriesNaming(MongockTemplate mongockTemplate) {
    Update update = new Update();
    update.rename("tags", "categories");
    mongockTemplate.updateMulti(Query.query(Criteria.where("_id").exists(true)), update, Post.class);
  }

  @ChangeSet(order = "005", id = "updateTextField", author = "onarozhnyi")
  public void updateTextField(MongockTemplate mongockTemplate) {
    Update update = new Update();
    update.set("text", "");
    mongockTemplate.updateMulti(Query.query(Criteria.where("_id").exists(true)), update, Post.class);
  }

  @ChangeSet(order = "006", id = "updateUserId", author = "onarozhnyi")
  public void updateUserId(MongockTemplate mongockTemplate) {
    var users = mongockTemplate.find(Query.query(Criteria.where("_id").exists(true)), User.class);

    users.forEach(user -> {
          Query query = Query.query(Criteria.where("createdBy").is(user.getCreatedBy()));
          mongockTemplate.updateMulti(query, new Update().set("userId", user.getId()), Post.class);
        }
        );
  }
}
