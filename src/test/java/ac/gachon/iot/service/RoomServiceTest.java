package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.Device;
import ac.gachon.iot.domain.entity.Room;
import ac.gachon.iot.domain.entity.RoomDevice;
import ac.gachon.iot.domain.enums.DeviceStatus;
import ac.gachon.iot.domain.repository.RoomRepository;
import ac.gachon.iot.dto.AllRoomsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock RoomRepository roomRepository;

    @InjectMocks RoomService roomService;

    @Test
    @DisplayName("방 목록과 기기 정보를 정상 반환한다")
    void findAllRooms_normalCase() {
        Device device = mock(Device.class);
        given(device.getId()).willReturn(1L);
        given(device.getName()).willReturn("에어컨");

        RoomDevice roomDevice = mock(RoomDevice.class);
        given(roomDevice.getDevice()).willReturn(device);
        given(roomDevice.getStatus()).willReturn(DeviceStatus.ON);

        Room room = mock(Room.class);
        given(room.getId()).willReturn(10L);
        given(room.getName()).willReturn("거실");
        given(room.getRoomDevices()).willReturn(List.of(roomDevice));

        given(roomRepository.findAllWithDevices()).willReturn(List.of(room));

        List<AllRoomsResponse> result = roomService.findAllRooms();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRoomId()).isEqualTo(10L);
        assertThat(result.get(0).getRoomName()).isEqualTo("거실");
        assertThat(result.get(0).getDevices()).hasSize(1);
        assertThat(result.get(0).getDevices().get(0).getDeviceId()).isEqualTo(1L);
        assertThat(result.get(0).getDevices().get(0).getDeviceType()).isEqualTo("에어컨");
        assertThat(result.get(0).getDevices().get(0).getStatus()).isEqualTo("ON");
    }

    @Test
    @DisplayName("방이 없으면 빈 리스트를 반환한다")
    void findAllRooms_noRooms_returnsEmptyList() {
        given(roomRepository.findAllWithDevices()).willReturn(List.of());

        List<AllRoomsResponse> result = roomService.findAllRooms();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("기기가 없는 방이면 devices가 빈 리스트이다")
    void findAllRooms_roomWithNoDevices_returnsEmptyDevices() {
        Room room = mock(Room.class);
        given(room.getId()).willReturn(20L);
        given(room.getName()).willReturn("창고");
        given(room.getRoomDevices()).willReturn(List.of());

        given(roomRepository.findAllWithDevices()).willReturn(List.of(room));

        List<AllRoomsResponse> result = roomService.findAllRooms();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDevices()).isEmpty();
    }

    @Test
    @DisplayName("여러 방과 여러 기기를 모두 반환한다")
    void findAllRooms_multipleRoomsAndDevices() {
        Device light = mock(Device.class);
        given(light.getId()).willReturn(1L);
        given(light.getName()).willReturn("조명");

        Device aircon = mock(Device.class);
        given(aircon.getId()).willReturn(2L);
        given(aircon.getName()).willReturn("에어컨");

        RoomDevice rd1 = mock(RoomDevice.class);
        given(rd1.getDevice()).willReturn(light);
        given(rd1.getStatus()).willReturn(DeviceStatus.ON);

        RoomDevice rd2 = mock(RoomDevice.class);
        given(rd2.getDevice()).willReturn(aircon);
        given(rd2.getStatus()).willReturn(DeviceStatus.OFF);

        Room room1 = mock(Room.class);
        given(room1.getId()).willReturn(1L);
        given(room1.getName()).willReturn("거실");
        given(room1.getRoomDevices()).willReturn(List.of(rd1, rd2));

        Room room2 = mock(Room.class);
        given(room2.getId()).willReturn(2L);
        given(room2.getName()).willReturn("침실");
        given(room2.getRoomDevices()).willReturn(List.of(rd1));

        given(roomRepository.findAllWithDevices()).willReturn(List.of(room1, room2));

        List<AllRoomsResponse> result = roomService.findAllRooms();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDevices()).hasSize(2);
        assertThat(result.get(1).getDevices()).hasSize(1);
    }
}
