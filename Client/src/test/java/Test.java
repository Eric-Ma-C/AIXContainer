import org.zju.vipa.aix.container.client.utils.XmlUtils;
import org.zju.vipa.aix.container.common.message.GpuInfo;

/**
 * @Date: 2020/4/20 21:13
 * @Author: EricMa
 * @Description:
 */
public class Test {

    @org.junit.Test
    public void test(){
        GpuInfo info= XmlUtils.parseGpuInfo(str2);

        System.out.println(info);
    }

    String str1="Tue Jun  9 19:46:24 2020\n" +
        "+-----------------------------------------------------------------------------+\n" +
        "| NVIDIA-SMI 440.31       Driver Version: 440.31       CUDA Version: 10.2     |\n" +
        "|-------------------------------+----------------------+----------------------+\n" +
        "| GPU  Name        Persistence-M| Bus-Id        Disp.A | Volatile Uncorr. ECC |\n" +
        "| Fan  Temp  Perf  Pwr:Usage/Cap|         Memory-Usage | GPU-Util  Compute M. |\n" +
        "|===============================+======================+======================|\n" +
        "|   0  Quadro P5000        Off  | 00000000:03:00.0 Off |                  Off |\n" +
        "|ERR!   73C    P0   174W / 180W |  11492MiB / 16276MiB |    100%      Default |\n" +
        "+-------------------------------+----------------------+----------------------+\n" +
        "|   1  Tesla K20c          Off  | 00000000:04:00.0 Off |                    0 |\n" +
        "| 32%   43C    P8    16W / 225W |     11MiB /  4743MiB |      0%      Default |\n" +
        "+-------------------------------+----------------------+----------------------+\n" +
        "\n" +
        "+-----------------------------------------------------------------------------+\n" +
        "| Processes:                                                       GPU Memory |\n" +
        "|  GPU       PID   Type   Process name                             Usage      |\n" +
        "|=============================================================================|\n" +
        "|    0      8006      C   python                                      1331MiB |\n" +
        "|    0      8850      C   python                                      5189MiB |\n" +
        "|    0     12374      C   python                                      2691MiB |\n" +
        "|    0     12375      C   python                                      2269MiB |\n" +
        "+-----------------------------------------------------------------------------+";
    String str2="<?xml version=\"1.0\" ?>\n" +
        "<!DOCTYPE nvidia_smi_log SYSTEM \"nvsmi_device_v10.dtd\">\n" +
        "<nvidia_smi_log>\n" +
        "    <timestamp>Tue Jun  9 20:18:39 2020</timestamp>\n" +
        "        <driver_version>440.31</driver_version>\n" +
        "        <cuda_version>10.2</cuda_version>\n" +
        "        <attached_gpus>2</attached_gpus>\n" +
        "        <gpu id=\"00000000:03:00.0\">\n" +
        "    <product_name>Quadro P5000</product_name>\n" +
        "                <product_brand>Quadro</product_brand>\n" +
        "                <display_mode>Disabled</display_mode>\n" +
        "                <display_active>Disabled</display_active>\n" +
        "                <persistence_mode>Disabled</persistence_mode>\n" +
        "                <accounting_mode>Disabled</accounting_mode>\n" +
        "                <accounting_mode_buffer_size>4000</accounting_mode_buffer_size>\n" +
        "                <driver_model>\n" +
        "                        <current_dm>N/A</current_dm>\n" +
        "                        <pending_dm>N/A</pending_dm>\n" +
        "                </driver_model>\n" +
        "                <serial>0324516238280</serial>\n" +
        "                <uuid>GPU-8dfe63db-0329-59c0-afec-8fbc97dfc026</uuid>\n" +
        "                <minor_number>0</minor_number>\n" +
        "                <vbios_version>86.04.40.00.09</vbios_version>\n" +
        "                <multigpu_board>No</multigpu_board>\n" +
        "                <board_id>0x300</board_id>\n" +
        "                <gpu_part_number>900-5G413-0100-000</gpu_part_number>\n" +
        "                <inforom_version>\n" +
        "                        <img_version>G413.0500.00.02</img_version>\n" +
        "                        <oem_object>1.1</oem_object>\n" +
        "                        <ecc_object>4.1</ecc_object>\n" +
        "                        <pwr_object>N/A</pwr_object>\n" +
        "                </inforom_version>\n" +
        "                <gpu_operation_mode>\n" +
        "                        <current_gom>N/A</current_gom>\n" +
        "                        <pending_gom>N/A</pending_gom>\n" +
        "                </gpu_operation_mode>\n" +
        "                <gpu_virtualization_mode>\n" +
        "                        <virtualization_mode>None</virtualization_mode>\n" +
        "                        <host_vgpu_mode>N/A</host_vgpu_mode>\n" +
        "                </gpu_virtualization_mode>\n" +
        "                <ibmnpu>\n" +
        "                        <relaxed_ordering_mode>N/A</relaxed_ordering_mode>\n" +
        "                </ibmnpu>\n" +
        "                <pci>\n" +
        "                        <pci_bus>03</pci_bus>\n" +
        "                        <pci_device>00</pci_device>\n" +
        "                        <pci_domain>0000</pci_domain>\n" +
        "                        <pci_device_id>1BB010DE</pci_device_id>\n" +
        "                        <pci_bus_id>00000000:03:00.0</pci_bus_id>\n" +
        "                        <pci_sub_system_id>11B21028</pci_sub_system_id>\n" +
        "                        <pci_gpu_link_info>\n" +
        "                                <pcie_gen>\n" +
        "                                        <max_link_gen>3</max_link_gen>\n" +
        "                                        <current_link_gen>3</current_link_gen>\n" +
        "                                </pcie_gen>\n" +
        "                                <link_widths>\n" +
        "                                        <max_link_width>16x</max_link_width>\n" +
        "                                        <current_link_width>16x</current_link_width>\n" +
        "                                </link_widths>\n" +
        "                        </pci_gpu_link_info>\n" +
        "                        <pci_bridge_chip>\n" +
        "                                <bridge_chip_type>N/A</bridge_chip_type>\n" +
        "                                <bridge_chip_fw>N/A</bridge_chip_fw>\n" +
        "                        </pci_bridge_chip>\n" +
        "                        <replay_counter>0</replay_counter>\n" +
        "                        <replay_rollover_counter>0</replay_rollover_counter>\n" +
        "                        <tx_util>393000 KB/s</tx_util>\n" +
        "                        <rx_util>2999000 KB/s</rx_util>\n" +
        "                </pci>\n" +
        "    <fan_speed>Unknown Error</fan_speed>\n" +
        "                <performance_state>P0</performance_state>\n" +
        "                <clocks_throttle_reasons>\n" +
        "    <clocks_throttle_reason_gpu_idle> Not Active</clocks_throttle_reason_gpu_idle>\n" +
        "    <clocks_throttle_reason_applications_clocks_setting>Not Active</clocks_throttle_reason_applications_clocks_setting>\n" +
        "    <clocks_throttle_reason_sw_power_cap>Not Active</clocks_throttle_reason_sw_power_cap>\n" +
        "    <clocks_throttle_reason_hw_slowdown>Not Active</clocks_throttle_reason_hw_slowdown>\n" +
        "    <clocks_throttle_reason_hw_thermal_slowdown>Not Active</clocks_throttle_reason_hw_thermal_slowdown>\n" +
        "    <clocks_throttle_reason_hw_power_brake_slowdown>Not Active</clocks_throttle_reason_hw_power_brake_slowdown>\n" +
        "    <clocks_throttle_reason_sync_boost>Not Active</clocks_throttle_reason_sync_boost>\n" +
        "    <clocks_throttle_reason_sw_thermal_slowdown>Not Active</clocks_throttle_reason_sw_thermal_slowdown>\n" +
        "    <clocks_throttle_reason_display_clocks_setting>Not Active</clocks_throttle_reason_display_clocks_setting>\n" +
        "                </clocks_throttle_reasons>\n" +
        "                <fb_memory_usage>\n" +
        "                        <total>16276 MiB</total>\n" +
        "                        <used>16047 MiB</used>\n" +
        "                        <free>229 MiB</free>\n" +
        "                </fb_memory_usage>\n" +
        "                <bar1_memory_usage>\n" +
        "                        <total>256 MiB</total>\n" +
        "                        <used>2 MiB</used>\n" +
        "                        <free>254 MiB</free>\n" +
        "                </bar1_memory_usage>\n" +
        "                <compute_mode>Default</compute_mode>\n" +
        "                <utilization>\n" +
        "                        <gpu_util>100 %</gpu_util>\n" +
        "                        <memory_util>50 %</memory_util>\n" +
        "                        <encoder_util>0 %</encoder_util>\n" +
        "                        <decoder_util>0 %</decoder_util>\n" +
        "                </utilization>\n" +
        "                <encoder_stats>\n" +
        "                        <session_count>0</session_count>\n" +
        "                        <average_fps>0</average_fps>\n" +
        "                        <average_latency>0</average_latency>\n" +
        "                </encoder_stats>\n" +
        "                <fbc_stats>\n" +
        "                        <session_count>0</session_count>\n" +
        "                        <average_fps>0</average_fps>\n" +
        "                        <average_latency>0</average_latency>\n" +
        "                </fbc_stats>\n" +
        "                <ecc_mode>\n" +
        "                        <current_ecc>Disabled</current_ecc>\n" +
        "                        <pending_ecc>Disabled</pending_ecc>\n" +
        "                </ecc_mode>\n" +
        "                <ecc_errors>\n" +
        "                        <volatile>\n" +
        "                                <single_bit>\n" +
        "                                        <device_memory>N/A</device_memory>\n" +
        "                                        <register_file>N/A</register_file>\n" +
        "                                        <l1_cache>N/A</l1_cache>\n" +
        "                                        <l2_cache>N/A</l2_cache>\n" +
        "                                        <texture_memory>N/A</texture_memory>\n" +
        "                                        <texture_shm>N/A</texture_shm>\n" +
        "                                        <cbu>N/A</cbu>\n" +
        "                                        <total>N/A</total>\n" +
        "                                </single_bit>\n" +
        "                                <double_bit>\n" +
        "                                        <device_memory>N/A</device_memory>\n" +
        "                                        <register_file>N/A</register_file>\n" +
        "                                        <l1_cache>N/A</l1_cache>\n" +
        "                                        <l2_cache>N/A</l2_cache>\n" +
        "                                        <texture_memory>N/A</texture_memory>\n" +
        "                                        <texture_shm>N/A</texture_shm>\n" +
        "                                        <cbu>N/A</cbu>\n" +
        "                                        <total>N/A</total>\n" +
        "                                </double_bit>\n" +
        "                        </volatile>\n" +
        "                        <aggregate>\n" +
        "                                <single_bit>\n" +
        "                                        <device_memory>N/A</device_memory>\n" +
        "                                        <register_file>N/A</register_file>\n" +
        "                                        <l1_cache>N/A</l1_cache>\n" +
        "                                        <l2_cache>N/A</l2_cache>\n" +
        "                                        <texture_memory>N/A</texture_memory>\n" +
        "                                        <texture_shm>N/A</texture_shm>\n" +
        "                                        <cbu>N/A</cbu>\n" +
        "                                        <total>N/A</total>\n" +
        "                                </single_bit>\n" +
        "                                <double_bit>\n" +
        "                                        <device_memory>N/A</device_memory>\n" +
        "                                        <register_file>N/A</register_file>\n" +
        "                                        <l1_cache>N/A</l1_cache>\n" +
        "                                        <l2_cache>N/A</l2_cache>\n" +
        "                                        <texture_memory>N/A</texture_memory>\n" +
        "                                        <texture_shm>N/A</texture_shm>\n" +
        "                                        <cbu>N/A</cbu>\n" +
        "                                        <total>N/A</total>\n" +
        "                                </double_bit>\n" +
        "                        </aggregate>\n" +
        "                </ecc_errors>\n" +
        "                <retired_pages>\n" +
        "                        <multiple_single_bit_retirement>\n" +
        "                                <retired_count>N/A</retired_count>\n" +
        "                                <retired_pagelist>N/A</retired_pagelist>\n" +
        "                        </multiple_single_bit_retirement>\n" +
        "                        <double_bit_retirement>\n" +
        "                                <retired_count>N/A</retired_count>\n" +
        "                                <retired_pagelist>N/A</retired_pagelist>\n" +
        "                        </double_bit_retirement>\n" +
        "                        <pending_blacklist>N/A</pending_blacklist>\n" +
        "                        <pending_retirement>N/A</pending_retirement>\n" +
        "                </retired_pages>\n" +
        "                <temperature>\n" +
        "                        <gpu_temp>73 C</gpu_temp>\n" +
        "                        <gpu_temp_max_threshold>99 C</gpu_temp_max_threshold>\n" +
        "                        <gpu_temp_slow_threshold>96 C</gpu_temp_slow_threshold>\n" +
        "                        <gpu_temp_max_gpu_threshold>N/A</gpu_temp_max_gpu_threshold>\n" +
        "                        <memory_temp>N/A</memory_temp>\n" +
        "                        <gpu_temp_max_mem_threshold>N/A</gpu_temp_max_mem_threshold>\n" +
        "                </temperature>\n" +
        "                <power_readings>\n" +
        "                        <power_state>P0</power_state>\n" +
        "                        <power_management>Supported</power_management>\n" +
        "                        <power_draw>159.47 W</power_draw>\n" +
        "                        <power_limit>180.00 W</power_limit>\n" +
        "                        <default_power_limit>180.00 W</default_power_limit>\n" +
        "                        <enforced_power_limit>180.00 W</enforced_power_limit>\n" +
        "                        <min_power_limit>90.00 W</min_power_limit>\n" +
        "                        <max_power_limit>180.00 W</max_power_limit>\n" +
        "                </power_readings>\n" +
        "                <clocks>\n" +
        "                        <graphics_clock>1657 MHz</graphics_clock>\n" +
        "                        <sm_clock>1657 MHz</sm_clock>\n" +
        "                        <mem_clock>4513 MHz</mem_clock>\n" +
        "                        <video_clock>1480 MHz</video_clock>\n" +
        "                </clocks>\n" +
        "                <applications_clocks>\n" +
        "                        <graphics_clock>1607 MHz</graphics_clock>\n" +
        "                        <mem_clock>4513 MHz</mem_clock>\n" +
        "                </applications_clocks>\n" +
        "                <default_applications_clocks>\n" +
        "                        <graphics_clock>1607 MHz</graphics_clock>\n" +
        "                        <mem_clock>4513 MHz</mem_clock>\n" +
        "                </default_applications_clocks>\n" +
        "                <max_clocks>\n" +
        "                        <graphics_clock>1733 MHz</graphics_clock>\n" +
        "                        <sm_clock>1733 MHz</sm_clock>\n" +
        "                        <mem_clock>4513 MHz</mem_clock>\n" +
        "                        <video_clock>1569 MHz</video_clock>\n" +
        "                </max_clocks>\n" +
        "                <max_customer_boost_clocks>\n" +
        "                        <graphics_clock>1733 MHz</graphics_clock>\n" +
        "                </max_customer_boost_clocks>\n" +
        "                <clock_policy>\n" +
        "                        <auto_boost>N/A</auto_boost>\n" +
        "                        <auto_boost_default>N/A</auto_boost_default>\n" +
        "                </clock_policy>\n" +
        "                <supported_clocks>\n" +
        "                        <supported_mem_clock>\n" +
        "                                <value>4513 MHz</value>\n" +
        "                                <supported_graphics_clock>1733 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1721 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1708 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1695 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1683 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1670 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1657 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1645 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1632 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1620 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1607 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1594 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1582 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1569 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1556 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1544 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1531 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1518 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1506 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1493 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1480 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1468 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1455 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1442 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1430 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1417 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1404 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1392 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1379 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1366 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1354 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1341 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1328 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1316 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1303 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1290 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1278 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1265 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1252 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1240 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1227 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1215 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1202 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1189 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1177 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1164 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1151 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1139 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1126 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1113 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1101 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1088 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1075 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1063 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1050 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1037 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1025 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1012 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>999 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>987 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>974 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>961 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>949 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>936 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>923 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>911 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>898 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>885 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>873 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>860 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>847 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>835 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>822 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>810 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>797 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>784 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>772 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>759 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>746 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>734 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>721 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>708 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>696 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>683 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>670 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>658 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>645 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>632 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>620 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>607 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>594 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>582 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>569 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>556 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>544 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>531 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>518 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>506 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>493 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>480 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>468 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>455 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>442 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>430 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>417 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>405 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>392 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>379 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>367 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>354 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>341 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>329 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>316 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>303 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>291 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>278 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>265 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>253 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>240 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>227 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>215 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>202 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>189 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>177 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>164 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>151 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>139 MHz</supported_graphics_clock>\n" +
        "                        </supported_mem_clock>\n" +
        "                        <supported_mem_clock>\n" +
        "                                <value>810 MHz</value>\n" +
        "                                <supported_graphics_clock>1733 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1721 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1708 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1695 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1683 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1670 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1657 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1645 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1632 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1620 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1607 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1594 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1582 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1569 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1556 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1544 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1531 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1518 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1506 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1493 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1480 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1468 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1455 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1442 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1430 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1417 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1404 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1392 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1379 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1366 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1354 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1341 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1328 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1316 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1303 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1290 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1278 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1265 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1252 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1240 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1227 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1215 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1202 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1189 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1177 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1164 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1151 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1139 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1126 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1113 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1101 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1088 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1075 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1063 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1050 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1037 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1025 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>1012 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>999 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>987 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>974 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>961 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>949 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>936 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>923 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>911 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>898 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>885 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>873 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>860 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>847 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>835 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>822 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>810 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>797 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>784 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>772 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>759 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>746 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>734 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>721 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>708 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>696 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>683 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>670 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>658 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>645 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>632 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>620 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>607 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>594 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>582 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>569 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>556 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>544 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>531 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>518 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>506 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>493 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>480 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>468 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>455 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>442 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>430 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>417 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>405 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>392 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>379 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>367 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>354 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>341 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>329 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>316 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>303 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>291 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>278 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>265 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>253 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>240 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>227 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>215 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>202 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>189 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>177 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>164 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>151 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>139 MHz</supported_graphics_clock>\n" +
        "                        </supported_mem_clock>\n" +
        "                        <supported_mem_clock>\n" +
        "                                <value>405 MHz</value>\n" +
        "                                <supported_graphics_clock>607 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>594 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>582 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>569 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>556 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>544 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>531 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>518 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>506 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>493 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>480 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>468 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>455 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>442 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>430 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>417 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>405 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>392 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>379 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>367 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>354 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>341 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>329 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>316 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>303 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>291 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>278 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>265 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>253 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>240 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>227 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>215 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>202 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>189 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>177 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>164 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>151 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>139 MHz</supported_graphics_clock>\n" +
        "                        </supported_mem_clock>\n" +
        "                </supported_clocks>\n" +
        "                <processes>\n" +
        "                        <process_info>\n" +
        "                                <pid>3263</pid>\n" +
        "                                <type>C</type>\n" +
        "                                <process_name>python</process_name>\n" +
        "                                <used_memory>2691 MiB</used_memory>\n" +
        "                        </process_info>\n" +
        "                        <process_info>\n" +
        "                                <pid>3264</pid>\n" +
        "                                <type>C</type>\n" +
        "                                <process_name>python</process_name>\n" +
        "                                <used_memory>2691 MiB</used_memory>\n" +
        "                        </process_info>\n" +
        "                        <process_info>\n" +
        "                                <pid>8850</pid>\n" +
        "                                <type>C</type>\n" +
        "                                <process_name>python</process_name>\n" +
        "                                <used_memory>5189 MiB</used_memory>\n" +
        "                        </process_info>\n" +
        "                        <process_info>\n" +
        "                                <pid>9620</pid>\n" +
        "                                <type>C</type>\n" +
        "                                <process_name>python</process_name>\n" +
        "                                <used_memory>4133 MiB</used_memory>\n" +
        "                        </process_info>\n" +
        "                        <process_info>\n" +
        "                                <pid>29690</pid>\n" +
        "                                <type>C</type>\n" +
        "                                <process_name>python</process_name>\n" +
        "                                <used_memory>1331 MiB</used_memory>\n" +
        "                        </process_info>\n" +
        "                </processes>\n" +
        "                <accounted_processes>\n" +
        "                </accounted_processes>\n" +
        "        </gpu>\n" +
        "\n" +
        "        <gpu id=\"00000000:04:00.0\">\n" +
        "    <product_name>Tesla K20c</product_name>\n" +
        "                <product_brand>Tesla</product_brand>\n" +
        "                <display_mode>Disabled</display_mode>\n" +
        "                <display_active>Disabled</display_active>\n" +
        "                <persistence_mode>Disabled</persistence_mode>\n" +
        "                <accounting_mode>Disabled</accounting_mode>\n" +
        "                <accounting_mode_buffer_size>4000</accounting_mode_buffer_size>\n" +
        "                <driver_model>\n" +
        "                        <current_dm>N/A</current_dm>\n" +
        "                        <pending_dm>N/A</pending_dm>\n" +
        "                </driver_model>\n" +
        "                <serial>0325213055927</serial>\n" +
        "                <uuid>GPU-a6fd20d3-622b-8bcc-39c6-cde95c6517ce</uuid>\n" +
        "                <minor_number>1</minor_number>\n" +
        "                <vbios_version>80.10.39.00.06</vbios_version>\n" +
        "                <multigpu_board>No</multigpu_board>\n" +
        "                <board_id>0x400</board_id>\n" +
        "                <gpu_part_number>900-22081-2220-000</gpu_part_number>\n" +
        "                <inforom_version>\n" +
        "                        <img_version>2081.0204.00.07</img_version>\n" +
        "                        <oem_object>1.1</oem_object>\n" +
        "                        <ecc_object>3.0</ecc_object>\n" +
        "                        <pwr_object>N/A</pwr_object>\n" +
        "                </inforom_version>\n" +
        "                <gpu_operation_mode>\n" +
        "                        <current_gom>N/A</current_gom>\n" +
        "                        <pending_gom>N/A</pending_gom>\n" +
        "                </gpu_operation_mode>\n" +
        "                <gpu_virtualization_mode>\n" +
        "                        <virtualization_mode>None</virtualization_mode>\n" +
        "                        <host_vgpu_mode>N/A</host_vgpu_mode>\n" +
        "                </gpu_virtualization_mode>\n" +
        "                <ibmnpu>\n" +
        "                        <relaxed_ordering_mode>N/A</relaxed_ordering_mode>\n" +
        "                </ibmnpu>\n" +
        "                <pci>\n" +
        "                        <pci_bus>04</pci_bus>\n" +
        "                        <pci_device>00</pci_device>\n" +
        "                        <pci_domain>0000</pci_domain>\n" +
        "                        <pci_device_id>102210DE</pci_device_id>\n" +
        "                        <pci_bus_id>00000000:04:00.0</pci_bus_id>\n" +
        "                        <pci_sub_system_id>098210DE</pci_sub_system_id>\n" +
        "                        <pci_gpu_link_info>\n" +
        "                                <pcie_gen>\n" +
        "                                        <max_link_gen>2</max_link_gen>\n" +
        "                                        <current_link_gen>1</current_link_gen>\n" +
        "                                </pcie_gen>\n" +
        "                                <link_widths>\n" +
        "                                        <max_link_width>16x</max_link_width>\n" +
        "                                        <current_link_width>16x</current_link_width>\n" +
        "                                </link_widths>\n" +
        "                        </pci_gpu_link_info>\n" +
        "                        <pci_bridge_chip>\n" +
        "                                <bridge_chip_type>N/A</bridge_chip_type>\n" +
        "                                <bridge_chip_fw>N/A</bridge_chip_fw>\n" +
        "                        </pci_bridge_chip>\n" +
        "                        <replay_counter>0</replay_counter>\n" +
        "                        <replay_rollover_counter>0</replay_rollover_counter>\n" +
        "                        <tx_util>N/A</tx_util>\n" +
        "                        <rx_util>N/A</rx_util>\n" +
        "                </pci>\n" +
        "                <fan_speed>32 %</fan_speed>\n" +
        "                <performance_state>P8</performance_state>\n" +
        "                <clocks_throttle_reasons>\n" +
        "                        <clocks_throttle_reason_gpu_idle>Active</clocks_throttle_reason_gpu_idle>\n" +
        "    <clocks_throttle_reason_applications_clocks_setting>Not Active</clocks_throttle_reason_applications_clocks_setting>\n" +
        "    <clocks_throttle_reason_sw_power_cap>Not Active</clocks_throttle_reason_sw_power_cap>\n" +
        "    <clocks_throttle_reason_hw_slowdown>Not Active</clocks_throttle_reason_hw_slowdown>\n" +
        "                        <clocks_throttle_reason_hw_thermal_slowdown>N/A</clocks_throttle_reason_hw_thermal_slowdown>\n" +
        "                        <clocks_throttle_reason_hw_power_brake_slowdown>N/A</clocks_throttle_reason_hw_power_brake_slowdown>\n" +
        "    <clocks_throttle_reason_sync_boost>Not Active</clocks_throttle_reason_sync_boost>\n" +
        "    <clocks_throttle_reason_sw_thermal_slowdown>Not Active</clocks_throttle_reason_sw_thermal_slowdown>\n" +
        "    <clocks_throttle_reason_display_clocks_setting>Not Active</clocks_throttle_reason_display_clocks_setting>\n" +
        "                </clocks_throttle_reasons>\n" +
        "                <fb_memory_usage>\n" +
        "                        <total>4743 MiB</total>\n" +
        "                        <used>11 MiB</used>\n" +
        "                        <free>4732 MiB</free>\n" +
        "                </fb_memory_usage>\n" +
        "                <bar1_memory_usage>\n" +
        "                        <total>256 MiB</total>\n" +
        "                        <used>2 MiB</used>\n" +
        "                        <free>254 MiB</free>\n" +
        "                </bar1_memory_usage>\n" +
        "                <compute_mode>Default</compute_mode>\n" +
        "                <utilization>\n" +
        "                        <gpu_util>0 %</gpu_util>\n" +
        "                        <memory_util>0 %</memory_util>\n" +
        "                        <encoder_util>0 %</encoder_util>\n" +
        "                        <decoder_util>0 %</decoder_util>\n" +
        "                </utilization>\n" +
        "                <encoder_stats>\n" +
        "                        <session_count>0</session_count>\n" +
        "                        <average_fps>0</average_fps>\n" +
        "                        <average_latency>0</average_latency>\n" +
        "                </encoder_stats>\n" +
        "                <fbc_stats>\n" +
        "                        <session_count>0</session_count>\n" +
        "                        <average_fps>0</average_fps>\n" +
        "                        <average_latency>0</average_latency>\n" +
        "                </fbc_stats>\n" +
        "                <ecc_mode>\n" +
        "                        <current_ecc>Enabled</current_ecc>\n" +
        "                        <pending_ecc>Enabled</pending_ecc>\n" +
        "                </ecc_mode>\n" +
        "                <ecc_errors>\n" +
        "                        <volatile>\n" +
        "                                <single_bit>\n" +
        "                                        <device_memory>0</device_memory>\n" +
        "                                        <register_file>0</register_file>\n" +
        "                                        <l1_cache>0</l1_cache>\n" +
        "                                        <l2_cache>0</l2_cache>\n" +
        "                                        <texture_memory>0</texture_memory>\n" +
        "                                        <texture_shm>N/A</texture_shm>\n" +
        "                                        <cbu>N/A</cbu>\n" +
        "                                        <total>0</total>\n" +
        "                                </single_bit>\n" +
        "                                <double_bit>\n" +
        "                                        <device_memory>0</device_memory>\n" +
        "                                        <register_file>0</register_file>\n" +
        "                                        <l1_cache>0</l1_cache>\n" +
        "                                        <l2_cache>0</l2_cache>\n" +
        "                                        <texture_memory>0</texture_memory>\n" +
        "                                        <texture_shm>N/A</texture_shm>\n" +
        "                                        <cbu>N/A</cbu>\n" +
        "                                        <total>0</total>\n" +
        "                                </double_bit>\n" +
        "                        </volatile>\n" +
        "                        <aggregate>\n" +
        "                                <single_bit>\n" +
        "                                        <device_memory>0</device_memory>\n" +
        "                                        <register_file>0</register_file>\n" +
        "                                        <l1_cache>0</l1_cache>\n" +
        "                                        <l2_cache>0</l2_cache>\n" +
        "                                        <texture_memory>0</texture_memory>\n" +
        "                                        <texture_shm>N/A</texture_shm>\n" +
        "                                        <cbu>N/A</cbu>\n" +
        "                                        <total>0</total>\n" +
        "                                </single_bit>\n" +
        "                                <double_bit>\n" +
        "                                        <device_memory>0</device_memory>\n" +
        "                                        <register_file>0</register_file>\n" +
        "                                        <l1_cache>0</l1_cache>\n" +
        "                                        <l2_cache>0</l2_cache>\n" +
        "                                        <texture_memory>0</texture_memory>\n" +
        "                                        <texture_shm>N/A</texture_shm>\n" +
        "                                        <cbu>N/A</cbu>\n" +
        "                                        <total>0</total>\n" +
        "                                </double_bit>\n" +
        "                        </aggregate>\n" +
        "                </ecc_errors>\n" +
        "                <retired_pages>\n" +
        "                        <multiple_single_bit_retirement>\n" +
        "                                <retired_count>0</retired_count>\n" +
        "                                <retired_pagelist>\n" +
        "                                </retired_pagelist>\n" +
        "                        </multiple_single_bit_retirement>\n" +
        "                        <double_bit_retirement>\n" +
        "                                <retired_count>0</retired_count>\n" +
        "                                <retired_pagelist>\n" +
        "                                </retired_pagelist>\n" +
        "                        </double_bit_retirement>\n" +
        "                        <pending_blacklist>No</pending_blacklist>\n" +
        "                        <pending_retirement>No</pending_retirement>\n" +
        "                </retired_pages>\n" +
        "                <temperature>\n" +
        "                        <gpu_temp>43 C</gpu_temp>\n" +
        "                        <gpu_temp_max_threshold>95 C</gpu_temp_max_threshold>\n" +
        "                        <gpu_temp_slow_threshold>90 C</gpu_temp_slow_threshold>\n" +
        "                        <gpu_temp_max_gpu_threshold>N/A</gpu_temp_max_gpu_threshold>\n" +
        "                        <memory_temp>N/A</memory_temp>\n" +
        "                        <gpu_temp_max_mem_threshold>N/A</gpu_temp_max_mem_threshold>\n" +
        "                </temperature>\n" +
        "                <power_readings>\n" +
        "                        <power_state>P8</power_state>\n" +
        "                        <power_management>Supported</power_management>\n" +
        "                        <power_draw>16.78 W</power_draw>\n" +
        "                        <power_limit>225.00 W</power_limit>\n" +
        "                        <default_power_limit>225.00 W</default_power_limit>\n" +
        "                        <enforced_power_limit>225.00 W</enforced_power_limit>\n" +
        "                        <min_power_limit>150.00 W</min_power_limit>\n" +
        "                        <max_power_limit>225.00 W</max_power_limit>\n" +
        "                </power_readings>\n" +
        "                <clocks>\n" +
        "                        <graphics_clock>324 MHz</graphics_clock>\n" +
        "                        <sm_clock>324 MHz</sm_clock>\n" +
        "                        <mem_clock>324 MHz</mem_clock>\n" +
        "                        <video_clock>405 MHz</video_clock>\n" +
        "                </clocks>\n" +
        "                <applications_clocks>\n" +
        "                        <graphics_clock>705 MHz</graphics_clock>\n" +
        "                        <mem_clock>2600 MHz</mem_clock>\n" +
        "                </applications_clocks>\n" +
        "                <default_applications_clocks>\n" +
        "                        <graphics_clock>705 MHz</graphics_clock>\n" +
        "                        <mem_clock>2600 MHz</mem_clock>\n" +
        "                </default_applications_clocks>\n" +
        "                <max_clocks>\n" +
        "                        <graphics_clock>758 MHz</graphics_clock>\n" +
        "                        <sm_clock>758 MHz</sm_clock>\n" +
        "                        <mem_clock>2600 MHz</mem_clock>\n" +
        "                        <video_clock>540 MHz</video_clock>\n" +
        "                </max_clocks>\n" +
        "                <max_customer_boost_clocks>\n" +
        "                        <graphics_clock>N/A</graphics_clock>\n" +
        "                </max_customer_boost_clocks>\n" +
        "                <clock_policy>\n" +
        "                        <auto_boost>N/A</auto_boost>\n" +
        "                        <auto_boost_default>N/A</auto_boost_default>\n" +
        "                </clock_policy>\n" +
        "                <supported_clocks>\n" +
        "                        <supported_mem_clock>\n" +
        "                                <value>2600 MHz</value>\n" +
        "                                <supported_graphics_clock>758 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>705 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>666 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>640 MHz</supported_graphics_clock>\n" +
        "                                <supported_graphics_clock>614 MHz</supported_graphics_clock>\n" +
        "                        </supported_mem_clock>\n" +
        "                        <supported_mem_clock>\n" +
        "                                <value>324 MHz</value>\n" +
        "                                <supported_graphics_clock>324 MHz</supported_graphics_clock>\n" +
        "                        </supported_mem_clock>\n" +
        "                </supported_clocks>\n" +
        "                <processes>\n" +
        "                </processes>\n" +
        "                <accounted_processes>\n" +
        "                </accounted_processes>\n" +
        "        </gpu>\n" +
        "\n" +
        "</nvidia_smi_log>";

}

