package service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dtos.GiftCertificateDTO;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.util.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateDAO giftCertificateDAO;
    @Mock
    private Mapper mapper;

    private GiftCertificate giftCertificate;
    private GiftCertificateDTO giftCertificateDTO;
    private GiftCertificateDTO certificateCopy;

    private static final int page = 1;
    private static final int size = 10;
    private static final long id = 1;

    @BeforeEach
    public void initUseCase() {
        giftCertificate = new GiftCertificate(1, "sportCertificate", "to sport",
                "20$", "5 days");
        giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(1);
        giftCertificateDTO.setName("sportCertificate");
        giftCertificateDTO.setDuration("5 days");
        giftCertificateDTO.setPrice("20$");
        giftCertificateDTO.setDescription("to sport");
        certificateCopy = new GiftCertificateDTO();
        certificateCopy.setDuration("5 days");
        certificateCopy.setId(1);
        certificateCopy.setName("sportCertificate");
        certificateCopy.setPrice("20$");
        certificateCopy.setDescription("to sport");
    }

    @Test
    public void testInsertGiftCertificate() {
        doNothing().when(giftCertificateDAO).insert(giftCertificate);
        giftCertificateDAO.insert(giftCertificate);
        verify(giftCertificateDAO, times(1)).insert(giftCertificate);
    }

    @Test
    public void testGetGiftCertificateByID() {
        Optional<GiftCertificate> optionalGiftCertificate = Optional.of(giftCertificate);
        when(giftCertificateDAO.read(id)).thenReturn(optionalGiftCertificate);
        Optional<GiftCertificate> newOptionalCertificate = giftCertificateDAO.read(id);
        GiftCertificate newCertificate = null;
        if (newOptionalCertificate.isPresent()){
            newCertificate = newOptionalCertificate.get();
        }
        verify(giftCertificateDAO, times(1)).read(id);
        assert newCertificate != null;
        assertThat(newCertificate.getName()).isEqualTo(giftCertificate.getName());
        assertThat(newCertificate).isNotNull();
    }

    @Test
    public void testGetCertificatesBySeveralTags() {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        GiftCertificate newCertificate = new GiftCertificate();
        Tag firstTag = new Tag(1, "test");
        Tag secondTag = new Tag(2, "tag");
        List<Tag> tags = new ArrayList<>();
        tags.add(firstTag);
        tags.add(secondTag);
        newCertificate.setTags(new HashSet<>(tags));
        newCertificate.setId(2);
        newCertificate.setName("sport");
        newCertificate.setLastUpdateDate("22.05.2022");
        newCertificate.setCreateDate("22.05.2022");
        newCertificate.setDuration("14 days");
        newCertificate.setPrice("10$");
        newCertificate.setDescription("description");
        giftCertificates.add(newCertificate);
        giftCertificates.add(giftCertificate);
        when(giftCertificateDAO.getCertificatesBySeveralTags(tags, page, size)).thenReturn(giftCertificates);

        List<GiftCertificate> testGiftCertificates = giftCertificateDAO.getCertificatesBySeveralTags(tags, page, size);
        verify(giftCertificateDAO, times(1)).getCertificatesBySeveralTags(tags, page, size);
        assertThat(testGiftCertificates).isNotNull();
        assertThat(testGiftCertificates.size()).isEqualTo(2);
        assertThat(testGiftCertificates.get(0).getName()).isEqualTo(newCertificate.getName());
    }

    @Test
    public void testGetGiftCertificatesDTOList() {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
        List<GiftCertificateDTO> giftCertificateDTOs = new ArrayList<>();
        giftCertificateDTOs.add(certificateCopy);
        when(mapper.mapToGiftCertificateDTOList(giftCertificates)).thenReturn(giftCertificateDTOs);
        when(giftCertificateDAO.readAll(page, size)).thenReturn(giftCertificates);

        List<GiftCertificate> newCertificates = giftCertificateDAO.readAll(page, size);
        List<GiftCertificateDTO> newDTOs = mapper.mapToGiftCertificateDTOList(newCertificates);
        verify(giftCertificateDAO, times(1)).readAll(page, size);
        verify(mapper, times(1)).mapToGiftCertificateDTOList(newCertificates);
        assertThat(newCertificates).isEqualTo(giftCertificates);
        assertThat(newDTOs).isEqualTo(giftCertificateDTOs);
    }

    @Test
    public void testDeleteGiftCertificate() throws RepositoryException {
        doNothing().when(giftCertificateDAO).delete(id);
        giftCertificateDAO.delete(id);
        verify(giftCertificateDAO, times(1)).delete(id);
    }

    @Test
    public void testDeleteGiftCertificate_RepositoryException() throws RepositoryException {
        doThrow(new RepositoryException()).when(giftCertificateDAO).delete(id);
        Assertions.assertThrows(RepositoryException.class, () -> giftCertificateDAO.delete(id));
        verify(giftCertificateDAO, times(1)).delete(id);
    }

    @Test
    public void testUpdateGiftCertificate() throws RepositoryException {
        when(mapper.mapToGiftCertificate(giftCertificateDTO)).thenReturn(giftCertificate);
        doNothing().when(giftCertificateDAO).update(id, giftCertificate);
        giftCertificateDAO.update(id, mapper.mapToGiftCertificate(giftCertificateDTO));
        verify(giftCertificateDAO, times(1)).update(id, mapper.mapToGiftCertificate(giftCertificateDTO));
    }

    @Test
    public void testUpdateGiftCertificate_RepositoryException() throws RepositoryException {
        doThrow(new RepositoryException()).when(giftCertificateDAO).update(id, giftCertificate);
        Assertions.assertThrows(RepositoryException.class, () -> giftCertificateDAO.update(id, giftCertificate));
        verify(giftCertificateDAO, times(1)).update(id, giftCertificate);
    }

    @Test
    public void testGetCertificatesDTOListAccordingToInputParams() {
        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("sort", "asc");
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
        List<GiftCertificateDTO> giftCertificateDTOs = new ArrayList<>();
        giftCertificateDTOs.add(certificateCopy);
        when(mapper.mapToGiftCertificateDTOList(giftCertificates)).thenReturn(giftCertificateDTOs);
        when(giftCertificateDAO.getCertificatesListAccordingToInputParams(searchParams, page, size)).thenReturn(giftCertificates);

        List<GiftCertificate> newGiftCertificate = giftCertificateDAO.getCertificatesListAccordingToInputParams(searchParams, page, size);
        List<GiftCertificateDTO> newDTOs = mapper.mapToGiftCertificateDTOList(newGiftCertificate);

        verify(mapper, times(1)).mapToGiftCertificateDTOList(giftCertificates);
        verify(giftCertificateDAO, times(1)).getCertificatesListAccordingToInputParams(searchParams, page, size);

        assertThat(newGiftCertificate).isEqualTo(giftCertificates);
        assertThat(newDTOs).isEqualTo(giftCertificateDTOs);
    }

    @Test
    public void testGetFilteredCertificateList() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("price", "200$");
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
        List<GiftCertificateDTO> giftCertificateDTOs = new ArrayList<>();
        giftCertificateDTOs.add(certificateCopy);
        when(mapper.mapToGiftCertificateDTOList(giftCertificates)).thenReturn(giftCertificateDTOs);
        when(giftCertificateDAO.getFilteredCertificateList(params)).thenReturn(giftCertificates);

        List<GiftCertificate> filteredCertificateList = giftCertificateDAO.getFilteredCertificateList(params);
        List<GiftCertificateDTO> filteredDTOs = mapper.mapToGiftCertificateDTOList(filteredCertificateList);

        verify(mapper, times(1)).mapToGiftCertificateDTOList(giftCertificates);
        verify(giftCertificateDAO, times(1)).getFilteredCertificateList(params);

        assertThat(filteredCertificateList).isEqualTo(giftCertificates);
        assertThat(filteredDTOs).isEqualTo(giftCertificateDTOs);
    }

}
