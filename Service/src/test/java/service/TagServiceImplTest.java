package service;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.domain.Tag;
import com.epam.esm.dtos.TagDTO;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.util.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagDAO tagDAO;
    @Mock
    private Mapper mapper;

    private TagDTO tagDTO;
    private Tag tag;

    private static final long id = 1;

    @BeforeEach
    public void initUseCase() {
        tag = new Tag(1, "tag");
        tagDTO = new TagDTO(1, "tag");
    }

    @Test
    public void testCreateTag() throws RepositoryException {
        when(mapper.mapToTag(tagDTO)).thenReturn(tag);
        doNothing().when(tagDAO).createTag(tag);
        tagDAO.createTag(mapper.mapToTag(tagDTO));
        verify(tagDAO, times(1)).createTag(mapper.mapToTag(tagDTO));
        verify(mapper, times(2)).mapToTag(tagDTO);
    }

    @Test
    public void testGetTagById() throws RepositoryException {
        when(mapper.mapToTagDTO(tag)).thenReturn(tagDTO);
        when(tagDAO.getTagById(id)).thenReturn(tag);
        Tag newTag = tagDAO.getTagById(id);
        TagDTO newTagDTO = mapper.mapToTagDTO(newTag);
        verify(tagDAO, times(1)).getTagById(id);
        verify(mapper, times(1)).mapToTagDTO(tag);
        assertThat(newTag).isEqualTo(tag);
        assertThat(newTagDTO).isEqualTo(tagDTO);
    }

    @Test
    public void testGetTagList() {
        int page = 1;
        int size = 10;
        List<TagDTO> tagDTOs = new ArrayList<>();
        tagDTOs.add(tagDTO);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(mapper.mapToTagDTOList(tags)).thenReturn(tagDTOs);
        when(tagDAO.getTagList(page, size)).thenReturn(tags);

        List<Tag> newTags = tagDAO.getTagList(page, size);
        List<TagDTO> newDTOs = mapper.mapToTagDTOList(newTags);

        verify(mapper, times(1)).mapToTagDTOList(tags);
        verify(tagDAO, times(1)).getTagList(page, size);

        assertThat(newTags).isEqualTo(tags);
        assertThat(newDTOs).isEqualTo(tagDTOs);
    }

    @Test
    public void testDeleteTag() throws RepositoryException {
        doNothing().when(tagDAO).deleteTag(id);
        tagDAO.deleteTag(id);
        verify(tagDAO, times(1)).deleteTag(id);
    }

    @Test
    public void testDeleteTag_RepositoryException() throws RepositoryException {
        doThrow(new RepositoryException()).when(tagDAO).deleteTag(id);
        Assertions.assertThrows(RepositoryException.class, () -> tagDAO.deleteTag(id));
        verify(tagDAO, times(1)).deleteTag(id);
    }
}
