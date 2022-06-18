package asknure.narozhnyi.core.service;

import asknure.narozhnyi.core.config.MessageProperties;
import asknure.narozhnyi.core.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

  private final JavaMailSender mailSender;
  private final MailProperties mailProperties;
  private final MessageProperties messageProperties;

  @SneakyThrows
  public void sendSimpleMessage(MessageDto messageDto)  {
    var mimeMessage = mailSender.createMimeMessage();
    var resource = new ClassPathResource("/logo/playstore.png");
    var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

    mimeMessageHelper.setSubject(messageProperties.getSubject());
    mimeMessageHelper.setFrom(mailProperties.getUsername());
    mimeMessageHelper.setText(populateFields(messageDto));
    mimeMessageHelper.setTo(messageDto.getReceiver());
    mimeMessageHelper.addInline("logo", resource);

    mailSender.send(mimeMessage);
  }

  private String populateFields(MessageDto messageDto) {
    return String.format(messageProperties.getText(), messageDto.getTitle());
  }
}
