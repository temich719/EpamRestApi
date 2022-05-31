package impl;

import com.epam.esm.config.DevRepositoryConfig;
import com.epam.esm.dao.GiftCertificateDAO;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = DevRepositoryConfig.class)
@ActiveProfiles("dev")
public class GiftCertificateDAOImplTest {

    @Autowired
    private GiftCertificateDAO giftCertificateDAO;

    private static final long id = 1;
    private static final int page = 1;
    private static final int size = 10;

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testRead() {
        GiftCertificate giftCertificate = giftCertificateDAO.read(id);
        assertThat(giftCertificate.getName()).isEqualTo("SportMaster");
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testGetCertificatesBySeveralTags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(3, "development"));
        tags.add(new Tag(4, "books"));
        List<GiftCertificate> giftCertificates = giftCertificateDAO.getCertificatesBySeveralTags(tags, page, size);
        assertThat(giftCertificates.get(0).getName()).isEqualTo("OZ");
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testReadAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDAO.readAll(page, size);
        assertThat(giftCertificates.get(0).getName()).isEqualTo("SportMaster");
        assertThat(giftCertificates.get(1).getName()).isEqualTo("DoDo Pizza");
        assertThat(giftCertificates.get(2).getName()).isEqualTo("OZ");
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testGetCertificatesListAccordingToInputParams() {
        Map<String, String> params = new HashMap<>();
        params.put("tagName", "sport");
        List<GiftCertificate> giftCertificates = giftCertificateDAO.getCertificatesListAccordingToInputParams(params, page, size);
        assertThat(giftCertificates.get(0).getName()).isEqualTo("SportMaster");
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testInsert() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(3, "development"));
        tags.add(new Tag(4, "books"));
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setDescription("test");
        giftCertificate.setLastUpdateDate("22.09.21");
        giftCertificate.setPrice("20$");
        giftCertificate.setDuration("11 days");
        giftCertificate.setName("Certificate");
        giftCertificate.setTags(new HashSet<>(tags));
        giftCertificate.setCreateDate("12.01.22");
        giftCertificateDAO.insert(giftCertificate);
        List<GiftCertificate> certificates = giftCertificateDAO.readAll(page, size);
        assertThat(certificates.get(certificates.size() - 1).getName()).isEqualTo(giftCertificate.getName());
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testDelete() throws RepositoryException {
        giftCertificateDAO.delete(id);
        List<GiftCertificate> giftCertificates = giftCertificateDAO.readAll(page, size);
        assertThat(giftCertificates.get(0).getName()).isNotEqualTo("SportMaster");
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testGetFilteredCertificateList() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        Object o = "50$";
        List<Object> objects = new ArrayList<>();
        objects.add(o);
        params.put("price", objects);
        List<GiftCertificate> giftCertificates = giftCertificateDAO.getFilteredCertificateList(params);
        assertThat(giftCertificates.get(0).getName()).isEqualTo("DoDo Pizza");
    }

}
