package asknure.narozhnyi.core.controller;

import asknure.narozhnyi.core.dto.UserDto;
import asknure.narozhnyi.core.dto.UserUpdateDto;
import asknure.narozhnyi.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

  private final UserService userService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<UserDto> findAll(@PageableDefault Pageable pageable) {
    return userService.findAll(pageable);
  }

  @GetMapping(path = "/{user-id}")
  @ResponseStatus(HttpStatus.OK)
  public UserDto findById(@PathVariable("user-id") String userId) {
    return userService.findById(userId);
  }

  @PutMapping("/{user-id}")
  public UserDto updateAvatar(
      @PathVariable("user-id") String userId,
      @RequestBody UserUpdateDto userUpdateDto) {
    return userService.updateAvatar(userId, userUpdateDto);
  }

  @GetMapping("/user-info")
  public UserDto obtainInfo() {
    return userService.obtainNewUser();
  }
}
