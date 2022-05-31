package impl;

import com.epam.esm.config.DevRepositoryConfig;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.RepositoryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DevRepositoryConfig.class)
@SpringBootTest
@ActiveProfiles("dev")
public class TagDAOImplTest {

    @Autowired
    private TagDAO tagDAO;

    private static final long id = 1;
    private static final int page = 1;
    private static final int size = 10;

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testCreateTag() throws RepositoryException {
        Tag tag = new Tag();
        long id = 5;
        tag.setId(id);
        tag.setName("beauty");
        tagDAO.createTag(tag);
        Tag newTag = tagDAO.getTagById(id);
        assertThat(tag.getName()).isEqualTo(newTag.getName());
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testGetTagById() throws RepositoryException {
        Tag tag = tagDAO.getTagById(id);
        assertThat(tag.getId()).isEqualTo(id);
        assertThat(tag.getName()).isEqualTo("sport");
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testGetTagList() {
        List<Tag> tags = tagDAO.getTagList(page, size);
        assertThat(tags.get(0).getName()).isEqualTo("sport");
        assertThat(tags.get(1).getName()).isEqualTo("food");
        assertThat(tags.get(2).getName()).isEqualTo("development");
        assertThat(tags.get(3).getName()).isEqualTo("books");
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testDeleteTag() throws RepositoryException {
        tagDAO.deleteTag(id);
        List<Tag> tags = tagDAO.getTagList(page, size);
        assertThat(tags.get(0).getName()).isNotEqualTo("sport");
    }

}
