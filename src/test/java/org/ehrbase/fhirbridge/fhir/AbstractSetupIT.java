package org.ehrbase.fhirbridge.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.nedap.archie.rm.datavalues.DvText;
import com.nedap.archie.rm.ehr.EhrStatus;
import com.nedap.archie.rm.generic.PartySelf;
import com.nedap.archie.rm.support.identification.HierObjectId;
import com.nedap.archie.rm.support.identification.PartyRef;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ehrbase.client.openehrclient.OpenEhrClientConfig;
import org.ehrbase.client.openehrclient.defaultrestclient.DefaultRestClient;
import org.ehrbase.fhirbridge.ehr.ResourceTemplateProvider;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.net.URI;
import java.net.URISyntaxException;


public abstract class AbstractSetupIT {

    static final String PATIENT_ID_TOKEN = "\\{\\{patientId\\}\\}";

    static String PATIENT_ID;

    protected final FhirContext context = FhirContext.forR4();

    IGenericClient client = context.newRestfulGenericClient("http://localhost:8888/fhir-bridge/fhir/");

    //protected final IGenericClient client = context.newRestfulGenericClient("http://localhost:8888/fhir-bridge/fhir/");

    @Autowired
    ResourceLoader resourceLoader;

    @BeforeAll
    static void setup() throws URISyntaxException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("myuser", "myPassword432"));

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        DefaultRestClient client = new DefaultRestClient(
                new OpenEhrClientConfig(new URI("http://localhost:8080/ehrbase/rest/openehr/v1/")),
                new ResourceTemplateProvider("classpath:/opt/*.opt"),
                httpClient);

        PATIENT_ID = UUID.randomUUID().toString();

        EhrStatus ehrStatus = new EhrStatus();
        ehrStatus.setSubject(new PartySelf(new PartyRef(new HierObjectId(PATIENT_ID), "demographic", "PERSON")));
        ehrStatus.setArchetypeNodeId("openEHR-EHR-EHR_STATUS.generic.v1");
        ehrStatus.setName(new DvText("Integration tests status"));
        client.ehrEndpoint().createEhr(ehrStatus);
    }

    protected MethodOutcome createAndGetOutcome(String location, String resourceUri) throws IOException {
        IGenericClient specificClient = context.newRestfulGenericClient("http://localhost:8888/fhir-bridge/fhir/"+resourceUri);
        String resource =  IOUtils.toString(new ClassPathResource(location).getInputStream(), StandardCharsets.UTF_8);
        MethodOutcome outcome = specificClient.create().resource(resource.replaceAll(PATIENT_ID_TOKEN, PATIENT_ID)).execute();
        return outcome;
    }
}
