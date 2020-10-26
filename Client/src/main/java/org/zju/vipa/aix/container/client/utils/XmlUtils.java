package org.zju.vipa.aix.container.client.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.zju.vipa.aix.container.common.message.GpuInfo;

import java.io.StringReader;
import java.util.List;

/**
 * @Date: 2020/6/9 20:25
 * @Author: EricMa
 * @Description: dom4j解析xml
 */
public class XmlUtils {
    public static GpuInfo parseGpuInfo(String info) {
        int index = info.indexOf("<nvidia_smi_log>");
        if (index<0){
            ClientLogUtils.error(true,"parseGpuInfo失败，info={}",info);
            return null;
        }
        info = info.substring(index);
        Document document;

        //利用dom4j读取xml
        SAXReader reader = new SAXReader();
        //忽略dtd文件校验！！！
        reader.setValidation(false);
//        reader.setEntityResolver(new EntityResolver() {
//            @Override
//            public InputSource resolveEntity(String publicId, String systemId) {
//                return new InputSource(new ByteArrayInputStream(info.getBytes()));
//            }
//        });
        try {
            document = reader.read(new StringReader(info));
        } catch (DocumentException e) {
            ClientExceptionUtils.handle(e);
            return null;
        }

        Element root = document.getRootElement();
        GpuInfo gpuInfo = new GpuInfo();
        gpuInfo.setDriverVersion(root.elementText("driver_version"));
        gpuInfo.setCudaVersion(root.elementText("cuda_version"));
        gpuInfo.setGpuNum(Integer.valueOf(root.elementText("attached_gpus")));

        List<Element> gpuList = root.elements("gpu");
        for (int i = 0; i < gpuList.size(); i++) {
            Element gpuNode = gpuList.get(i);
            GpuInfo.Gpu gpu = new GpuInfo.Gpu(String.valueOf(i),
                gpuNode.elementText("product_name"),
                gpuNode.element("temperature").elementText("gpu_temp"),
                gpuNode.element("power_readings").elementText("power_draw"),
                gpuNode.element("power_readings").elementText("power_limit"),
                gpuNode.element("fb_memory_usage").elementText("used"),
                gpuNode.element("fb_memory_usage").elementText("total"));

            List<Element> processList = gpuNode.element("processes").elements("process_info");
            for (int j = 0; j < processList.size(); j++) {
                Element processNode = processList.get(j);
                GpuInfo.Process process = new GpuInfo.Process(
                    processNode.elementText("pid"),
                    processNode.elementText("type"),
                    processNode.elementText("process_name"),
                    processNode.elementText("used_memory"));
                gpu.addProcess(process);
            }
            gpuInfo.addGpu(gpu);
        }

        return gpuInfo;
    }


//    <?xml version="1.0" ?>
//<!DOCTYPE nvidia_smi_log SYSTEM "nvsmi_device_v10.dtd">
//<nvidia_smi_log>
//    <timestamp>Tue Jun  9 20:18:39 2020</timestamp>
//        <driver_version>440.31</driver_version>
//        <cuda_version>10.2</cuda_version>
//        <attached_gpus>2</attached_gpus>
//        <gpu id="00000000:03:00.0">
//    <product_name>Quadro P5000</product_name>
//                <product_brand>Quadro</product_brand>
//                <display_mode>Disabled</display_mode>
//                <display_active>Disabled</display_active>
//                <persistence_mode>Disabled</persistence_mode>
//                <accounting_mode>Disabled</accounting_mode>
//                <accounting_mode_buffer_size>4000</accounting_mode_buffer_size>
//                <driver_model>
//                        <current_dm>N/A</current_dm>
//                        <pending_dm>N/A</pending_dm>
//                </driver_model>
//                <serial>0324516238280</serial>
//                <uuid>GPU-8dfe63db-0329-59c0-afec-8fbc97dfc026</uuid>
//                <minor_number>0</minor_number>
//                <vbios_version>86.04.40.00.09</vbios_version>
//                <multigpu_board>No</multigpu_board>
//                <board_id>0x300</board_id>
//                <gpu_part_number>900-5G413-0100-000</gpu_part_number>
//                <inforom_version>
//                        <img_version>G413.0500.00.02</img_version>
//                        <oem_object>1.1</oem_object>
//                        <ecc_object>4.1</ecc_object>
//                        <pwr_object>N/A</pwr_object>
//                </inforom_version>
//                <gpu_operation_mode>
//                        <current_gom>N/A</current_gom>
//                        <pending_gom>N/A</pending_gom>
//                </gpu_operation_mode>
//                <gpu_virtualization_mode>
//                        <virtualization_mode>None</virtualization_mode>
//                        <host_vgpu_mode>N/A</host_vgpu_mode>
//                </gpu_virtualization_mode>
//                <ibmnpu>
//                        <relaxed_ordering_mode>N/A</relaxed_ordering_mode>
//                </ibmnpu>
//                <pci>
//                        <pci_bus>03</pci_bus>
//                        <pci_device>00</pci_device>
//                        <pci_domain>0000</pci_domain>
//                        <pci_device_id>1BB010DE</pci_device_id>
//                        <pci_bus_id>00000000:03:00.0</pci_bus_id>
//                        <pci_sub_system_id>11B21028</pci_sub_system_id>
//                        <pci_gpu_link_info>
//                                <pcie_gen>
//                                        <max_link_gen>3</max_link_gen>
//                                        <current_link_gen>3</current_link_gen>
//                                </pcie_gen>
//                                <link_widths>
//                                        <max_link_width>16x</max_link_width>
//                                        <current_link_width>16x</current_link_width>
//                                </link_widths>
//                        </pci_gpu_link_info>
//                        <pci_bridge_chip>
//                                <bridge_chip_type>N/A</bridge_chip_type>
//                                <bridge_chip_fw>N/A</bridge_chip_fw>
//                        </pci_bridge_chip>
//                        <replay_counter>0</replay_counter>
//                        <replay_rollover_counter>0</replay_rollover_counter>
//                        <tx_util>393000 KB/s</tx_util>
//                        <rx_util>2999000 KB/s</rx_util>
//                </pci>
//    <fan_speed>Unknown Error</fan_speed>
//                <performance_state>P0</performance_state>
//                <clocks_throttle_reasons>
//    <clocks_throttle_reason_gpu_idle> Not Active</clocks_throttle_reason_gpu_idle>
//    <clocks_throttle_reason_applications_clocks_setting>Not Active</clocks_throttle_reason_applications_clocks_setting>
//    <clocks_throttle_reason_sw_power_cap>Not Active</clocks_throttle_reason_sw_power_cap>
//    <clocks_throttle_reason_hw_slowdown>Not Active</clocks_throttle_reason_hw_slowdown>
//    <clocks_throttle_reason_hw_thermal_slowdown>Not Active</clocks_throttle_reason_hw_thermal_slowdown>
//    <clocks_throttle_reason_hw_power_brake_slowdown>Not Active</clocks_throttle_reason_hw_power_brake_slowdown>
//    <clocks_throttle_reason_sync_boost>Not Active</clocks_throttle_reason_sync_boost>
//    <clocks_throttle_reason_sw_thermal_slowdown>Not Active</clocks_throttle_reason_sw_thermal_slowdown>
//    <clocks_throttle_reason_display_clocks_setting>Not Active</clocks_throttle_reason_display_clocks_setting>
//                </clocks_throttle_reasons>
//                <fb_memory_usage>
//                        <total>16276 MiB</total>
//                        <used>16047 MiB</used>
//                        <free>229 MiB</free>
//                </fb_memory_usage>
//                <bar1_memory_usage>
//                        <total>256 MiB</total>
//                        <used>2 MiB</used>
//                        <free>254 MiB</free>
//                </bar1_memory_usage>
//                <compute_mode>Default</compute_mode>
//                <utilization>
//                        <gpu_util>100 %</gpu_util>
//                        <memory_util>50 %</memory_util>
//                        <encoder_util>0 %</encoder_util>
//                        <decoder_util>0 %</decoder_util>
//                </utilization>
//                <encoder_stats>
//                        <session_count>0</session_count>
//                        <average_fps>0</average_fps>
//                        <average_latency>0</average_latency>
//                </encoder_stats>
//                <fbc_stats>
//                        <session_count>0</session_count>
//                        <average_fps>0</average_fps>
//                        <average_latency>0</average_latency>
//                </fbc_stats>
//                <ecc_mode>
//                        <current_ecc>Disabled</current_ecc>
//                        <pending_ecc>Disabled</pending_ecc>
//                </ecc_mode>
//                <ecc_errors>
//                        <volatile>
//                                <single_bit>
//                                        <device_memory>N/A</device_memory>
//                                        <register_file>N/A</register_file>
//                                        <l1_cache>N/A</l1_cache>
//                                        <l2_cache>N/A</l2_cache>
//                                        <texture_memory>N/A</texture_memory>
//                                        <texture_shm>N/A</texture_shm>
//                                        <cbu>N/A</cbu>
//                                        <total>N/A</total>
//                                </single_bit>
//                                <double_bit>
//                                        <device_memory>N/A</device_memory>
//                                        <register_file>N/A</register_file>
//                                        <l1_cache>N/A</l1_cache>
//                                        <l2_cache>N/A</l2_cache>
//                                        <texture_memory>N/A</texture_memory>
//                                        <texture_shm>N/A</texture_shm>
//                                        <cbu>N/A</cbu>
//                                        <total>N/A</total>
//                                </double_bit>
//                        </volatile>
//                        <aggregate>
//                                <single_bit>
//                                        <device_memory>N/A</device_memory>
//                                        <register_file>N/A</register_file>
//                                        <l1_cache>N/A</l1_cache>
//                                        <l2_cache>N/A</l2_cache>
//                                        <texture_memory>N/A</texture_memory>
//                                        <texture_shm>N/A</texture_shm>
//                                        <cbu>N/A</cbu>
//                                        <total>N/A</total>
//                                </single_bit>
//                                <double_bit>
//                                        <device_memory>N/A</device_memory>
//                                        <register_file>N/A</register_file>
//                                        <l1_cache>N/A</l1_cache>
//                                        <l2_cache>N/A</l2_cache>
//                                        <texture_memory>N/A</texture_memory>
//                                        <texture_shm>N/A</texture_shm>
//                                        <cbu>N/A</cbu>
//                                        <total>N/A</total>
//                                </double_bit>
//                        </aggregate>
//                </ecc_errors>
//                <retired_pages>
//                        <multiple_single_bit_retirement>
//                                <retired_count>N/A</retired_count>
//                                <retired_pagelist>N/A</retired_pagelist>
//                        </multiple_single_bit_retirement>
//                        <double_bit_retirement>
//                                <retired_count>N/A</retired_count>
//                                <retired_pagelist>N/A</retired_pagelist>
//                        </double_bit_retirement>
//                        <pending_blacklist>N/A</pending_blacklist>
//                        <pending_retirement>N/A</pending_retirement>
//                </retired_pages>
//                <temperature>
//                        <gpu_temp>73 C</gpu_temp>
//                        <gpu_temp_max_threshold>99 C</gpu_temp_max_threshold>
//                        <gpu_temp_slow_threshold>96 C</gpu_temp_slow_threshold>
//                        <gpu_temp_max_gpu_threshold>N/A</gpu_temp_max_gpu_threshold>
//                        <memory_temp>N/A</memory_temp>
//                        <gpu_temp_max_mem_threshold>N/A</gpu_temp_max_mem_threshold>
//                </temperature>
//                <power_readings>
//                        <power_state>P0</power_state>
//                        <power_management>Supported</power_management>
//                        <power_draw>159.47 W</power_draw>
//                        <power_limit>180.00 W</power_limit>
//                        <default_power_limit>180.00 W</default_power_limit>
//                        <enforced_power_limit>180.00 W</enforced_power_limit>
//                        <min_power_limit>90.00 W</min_power_limit>
//                        <max_power_limit>180.00 W</max_power_limit>
//                </power_readings>
//                <clocks>
//                        <graphics_clock>1657 MHz</graphics_clock>
//                        <sm_clock>1657 MHz</sm_clock>
//                        <mem_clock>4513 MHz</mem_clock>
//                        <video_clock>1480 MHz</video_clock>
//                </clocks>
//                <applications_clocks>
//                        <graphics_clock>1607 MHz</graphics_clock>
//                        <mem_clock>4513 MHz</mem_clock>
//                </applications_clocks>
//                <default_applications_clocks>
//                        <graphics_clock>1607 MHz</graphics_clock>
//                        <mem_clock>4513 MHz</mem_clock>
//                </default_applications_clocks>
//                <max_clocks>
//                        <graphics_clock>1733 MHz</graphics_clock>
//                        <sm_clock>1733 MHz</sm_clock>
//                        <mem_clock>4513 MHz</mem_clock>
//                        <video_clock>1569 MHz</video_clock>
//                </max_clocks>
//                <max_customer_boost_clocks>
//                        <graphics_clock>1733 MHz</graphics_clock>
//                </max_customer_boost_clocks>
//                <clock_policy>
//                        <auto_boost>N/A</auto_boost>
//                        <auto_boost_default>N/A</auto_boost_default>
//                </clock_policy>
//                <supported_clocks>
//                        <supported_mem_clock>
//                                <value>4513 MHz</value>
//                                <supported_graphics_clock>1733 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1721 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1708 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1695 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1683 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1670 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1657 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1645 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1632 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1620 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1607 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1594 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1582 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1569 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1556 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1544 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1531 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1518 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1506 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1493 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1480 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1468 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1455 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1442 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1430 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1417 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1404 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1392 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1379 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1366 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1354 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1341 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1328 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1316 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1303 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1290 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1278 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1265 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1252 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1240 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1227 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1215 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1202 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1189 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1177 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1164 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1151 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1139 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1126 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1113 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1101 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1088 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1075 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1063 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1050 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1037 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1025 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1012 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>999 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>987 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>974 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>961 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>949 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>936 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>923 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>911 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>898 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>885 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>873 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>860 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>847 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>835 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>822 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>810 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>797 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>784 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>772 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>759 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>746 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>734 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>721 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>708 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>696 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>683 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>670 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>658 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>645 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>632 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>620 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>607 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>594 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>582 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>569 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>556 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>544 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>531 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>518 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>506 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>493 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>480 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>468 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>455 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>442 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>430 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>417 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>405 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>392 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>379 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>367 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>354 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>341 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>329 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>316 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>303 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>291 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>278 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>265 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>253 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>240 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>227 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>215 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>202 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>189 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>177 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>164 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>151 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>139 MHz</supported_graphics_clock>
//                        </supported_mem_clock>
//                        <supported_mem_clock>
//                                <value>810 MHz</value>
//                                <supported_graphics_clock>1733 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1721 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1708 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1695 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1683 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1670 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1657 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1645 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1632 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1620 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1607 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1594 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1582 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1569 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1556 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1544 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1531 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1518 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1506 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1493 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1480 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1468 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1455 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1442 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1430 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1417 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1404 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1392 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1379 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1366 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1354 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1341 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1328 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1316 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1303 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1290 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1278 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1265 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1252 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1240 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1227 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1215 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1202 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1189 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1177 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1164 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1151 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1139 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1126 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1113 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1101 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1088 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1075 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1063 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1050 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1037 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1025 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>1012 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>999 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>987 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>974 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>961 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>949 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>936 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>923 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>911 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>898 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>885 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>873 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>860 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>847 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>835 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>822 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>810 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>797 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>784 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>772 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>759 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>746 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>734 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>721 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>708 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>696 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>683 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>670 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>658 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>645 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>632 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>620 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>607 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>594 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>582 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>569 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>556 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>544 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>531 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>518 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>506 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>493 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>480 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>468 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>455 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>442 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>430 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>417 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>405 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>392 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>379 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>367 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>354 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>341 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>329 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>316 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>303 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>291 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>278 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>265 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>253 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>240 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>227 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>215 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>202 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>189 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>177 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>164 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>151 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>139 MHz</supported_graphics_clock>
//                        </supported_mem_clock>
//                        <supported_mem_clock>
//                                <value>405 MHz</value>
//                                <supported_graphics_clock>607 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>594 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>582 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>569 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>556 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>544 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>531 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>518 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>506 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>493 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>480 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>468 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>455 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>442 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>430 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>417 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>405 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>392 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>379 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>367 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>354 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>341 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>329 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>316 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>303 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>291 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>278 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>265 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>253 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>240 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>227 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>215 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>202 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>189 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>177 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>164 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>151 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>139 MHz</supported_graphics_clock>
//                        </supported_mem_clock>
//                </supported_clocks>
//                <processes>
//                        <process_info>
//                                <pid>3263</pid>
//                                <type>C</type>
//                                <process_name>python</process_name>
//                                <used_memory>2691 MiB</used_memory>
//                        </process_info>
//                        <process_info>
//                                <pid>3264</pid>
//                                <type>C</type>
//                                <process_name>python</process_name>
//                                <used_memory>2691 MiB</used_memory>
//                        </process_info>
//                        <process_info>
//                                <pid>8850</pid>
//                                <type>C</type>
//                                <process_name>python</process_name>
//                                <used_memory>5189 MiB</used_memory>
//                        </process_info>
//                        <process_info>
//                                <pid>9620</pid>
//                                <type>C</type>
//                                <process_name>python</process_name>
//                                <used_memory>4133 MiB</used_memory>
//                        </process_info>
//                        <process_info>
//                                <pid>29690</pid>
//                                <type>C</type>
//                                <process_name>python</process_name>
//                                <used_memory>1331 MiB</used_memory>
//                        </process_info>
//                </processes>
//                <accounted_processes>
//                </accounted_processes>
//        </gpu>
//
//        <gpu id="00000000:04:00.0">
//    <product_name>Tesla K20c</product_name>
//                <product_brand>Tesla</product_brand>
//                <display_mode>Disabled</display_mode>
//                <display_active>Disabled</display_active>
//                <persistence_mode>Disabled</persistence_mode>
//                <accounting_mode>Disabled</accounting_mode>
//                <accounting_mode_buffer_size>4000</accounting_mode_buffer_size>
//                <driver_model>
//                        <current_dm>N/A</current_dm>
//                        <pending_dm>N/A</pending_dm>
//                </driver_model>
//                <serial>0325213055927</serial>
//                <uuid>GPU-a6fd20d3-622b-8bcc-39c6-cde95c6517ce</uuid>
//                <minor_number>1</minor_number>
//                <vbios_version>80.10.39.00.06</vbios_version>
//                <multigpu_board>No</multigpu_board>
//                <board_id>0x400</board_id>
//                <gpu_part_number>900-22081-2220-000</gpu_part_number>
//                <inforom_version>
//                        <img_version>2081.0204.00.07</img_version>
//                        <oem_object>1.1</oem_object>
//                        <ecc_object>3.0</ecc_object>
//                        <pwr_object>N/A</pwr_object>
//                </inforom_version>
//                <gpu_operation_mode>
//                        <current_gom>N/A</current_gom>
//                        <pending_gom>N/A</pending_gom>
//                </gpu_operation_mode>
//                <gpu_virtualization_mode>
//                        <virtualization_mode>None</virtualization_mode>
//                        <host_vgpu_mode>N/A</host_vgpu_mode>
//                </gpu_virtualization_mode>
//                <ibmnpu>
//                        <relaxed_ordering_mode>N/A</relaxed_ordering_mode>
//                </ibmnpu>
//                <pci>
//                        <pci_bus>04</pci_bus>
//                        <pci_device>00</pci_device>
//                        <pci_domain>0000</pci_domain>
//                        <pci_device_id>102210DE</pci_device_id>
//                        <pci_bus_id>00000000:04:00.0</pci_bus_id>
//                        <pci_sub_system_id>098210DE</pci_sub_system_id>
//                        <pci_gpu_link_info>
//                                <pcie_gen>
//                                        <max_link_gen>2</max_link_gen>
//                                        <current_link_gen>1</current_link_gen>
//                                </pcie_gen>
//                                <link_widths>
//                                        <max_link_width>16x</max_link_width>
//                                        <current_link_width>16x</current_link_width>
//                                </link_widths>
//                        </pci_gpu_link_info>
//                        <pci_bridge_chip>
//                                <bridge_chip_type>N/A</bridge_chip_type>
//                                <bridge_chip_fw>N/A</bridge_chip_fw>
//                        </pci_bridge_chip>
//                        <replay_counter>0</replay_counter>
//                        <replay_rollover_counter>0</replay_rollover_counter>
//                        <tx_util>N/A</tx_util>
//                        <rx_util>N/A</rx_util>
//                </pci>
//                <fan_speed>32 %</fan_speed>
//                <performance_state>P8</performance_state>
//                <clocks_throttle_reasons>
//                        <clocks_throttle_reason_gpu_idle>Active</clocks_throttle_reason_gpu_idle>
//    <clocks_throttle_reason_applications_clocks_setting>Not Active</clocks_throttle_reason_applications_clocks_setting>
//    <clocks_throttle_reason_sw_power_cap>Not Active</clocks_throttle_reason_sw_power_cap>
//    <clocks_throttle_reason_hw_slowdown>Not Active</clocks_throttle_reason_hw_slowdown>
//                        <clocks_throttle_reason_hw_thermal_slowdown>N/A</clocks_throttle_reason_hw_thermal_slowdown>
//                        <clocks_throttle_reason_hw_power_brake_slowdown>N/A</clocks_throttle_reason_hw_power_brake_slowdown>
//    <clocks_throttle_reason_sync_boost>Not Active</clocks_throttle_reason_sync_boost>
//    <clocks_throttle_reason_sw_thermal_slowdown>Not Active</clocks_throttle_reason_sw_thermal_slowdown>
//    <clocks_throttle_reason_display_clocks_setting>Not Active</clocks_throttle_reason_display_clocks_setting>
//                </clocks_throttle_reasons>
//                <fb_memory_usage>
//                        <total>4743 MiB</total>
//                        <used>11 MiB</used>
//                        <free>4732 MiB</free>
//                </fb_memory_usage>
//                <bar1_memory_usage>
//                        <total>256 MiB</total>
//                        <used>2 MiB</used>
//                        <free>254 MiB</free>
//                </bar1_memory_usage>
//                <compute_mode>Default</compute_mode>
//                <utilization>
//                        <gpu_util>0 %</gpu_util>
//                        <memory_util>0 %</memory_util>
//                        <encoder_util>0 %</encoder_util>
//                        <decoder_util>0 %</decoder_util>
//                </utilization>
//                <encoder_stats>
//                        <session_count>0</session_count>
//                        <average_fps>0</average_fps>
//                        <average_latency>0</average_latency>
//                </encoder_stats>
//                <fbc_stats>
//                        <session_count>0</session_count>
//                        <average_fps>0</average_fps>
//                        <average_latency>0</average_latency>
//                </fbc_stats>
//                <ecc_mode>
//                        <current_ecc>Enabled</current_ecc>
//                        <pending_ecc>Enabled</pending_ecc>
//                </ecc_mode>
//                <ecc_errors>
//                        <volatile>
//                                <single_bit>
//                                        <device_memory>0</device_memory>
//                                        <register_file>0</register_file>
//                                        <l1_cache>0</l1_cache>
//                                        <l2_cache>0</l2_cache>
//                                        <texture_memory>0</texture_memory>
//                                        <texture_shm>N/A</texture_shm>
//                                        <cbu>N/A</cbu>
//                                        <total>0</total>
//                                </single_bit>
//                                <double_bit>
//                                        <device_memory>0</device_memory>
//                                        <register_file>0</register_file>
//                                        <l1_cache>0</l1_cache>
//                                        <l2_cache>0</l2_cache>
//                                        <texture_memory>0</texture_memory>
//                                        <texture_shm>N/A</texture_shm>
//                                        <cbu>N/A</cbu>
//                                        <total>0</total>
//                                </double_bit>
//                        </volatile>
//                        <aggregate>
//                                <single_bit>
//                                        <device_memory>0</device_memory>
//                                        <register_file>0</register_file>
//                                        <l1_cache>0</l1_cache>
//                                        <l2_cache>0</l2_cache>
//                                        <texture_memory>0</texture_memory>
//                                        <texture_shm>N/A</texture_shm>
//                                        <cbu>N/A</cbu>
//                                        <total>0</total>
//                                </single_bit>
//                                <double_bit>
//                                        <device_memory>0</device_memory>
//                                        <register_file>0</register_file>
//                                        <l1_cache>0</l1_cache>
//                                        <l2_cache>0</l2_cache>
//                                        <texture_memory>0</texture_memory>
//                                        <texture_shm>N/A</texture_shm>
//                                        <cbu>N/A</cbu>
//                                        <total>0</total>
//                                </double_bit>
//                        </aggregate>
//                </ecc_errors>
//                <retired_pages>
//                        <multiple_single_bit_retirement>
//                                <retired_count>0</retired_count>
//                                <retired_pagelist>
//                                </retired_pagelist>
//                        </multiple_single_bit_retirement>
//                        <double_bit_retirement>
//                                <retired_count>0</retired_count>
//                                <retired_pagelist>
//                                </retired_pagelist>
//                        </double_bit_retirement>
//                        <pending_blacklist>No</pending_blacklist>
//                        <pending_retirement>No</pending_retirement>
//                </retired_pages>
//                <temperature>
//                        <gpu_temp>43 C</gpu_temp>
//                        <gpu_temp_max_threshold>95 C</gpu_temp_max_threshold>
//                        <gpu_temp_slow_threshold>90 C</gpu_temp_slow_threshold>
//                        <gpu_temp_max_gpu_threshold>N/A</gpu_temp_max_gpu_threshold>
//                        <memory_temp>N/A</memory_temp>
//                        <gpu_temp_max_mem_threshold>N/A</gpu_temp_max_mem_threshold>
//                </temperature>
//                <power_readings>
//                        <power_state>P8</power_state>
//                        <power_management>Supported</power_management>
//                        <power_draw>16.78 W</power_draw>
//                        <power_limit>225.00 W</power_limit>
//                        <default_power_limit>225.00 W</default_power_limit>
//                        <enforced_power_limit>225.00 W</enforced_power_limit>
//                        <min_power_limit>150.00 W</min_power_limit>
//                        <max_power_limit>225.00 W</max_power_limit>
//                </power_readings>
//                <clocks>
//                        <graphics_clock>324 MHz</graphics_clock>
//                        <sm_clock>324 MHz</sm_clock>
//                        <mem_clock>324 MHz</mem_clock>
//                        <video_clock>405 MHz</video_clock>
//                </clocks>
//                <applications_clocks>
//                        <graphics_clock>705 MHz</graphics_clock>
//                        <mem_clock>2600 MHz</mem_clock>
//                </applications_clocks>
//                <default_applications_clocks>
//                        <graphics_clock>705 MHz</graphics_clock>
//                        <mem_clock>2600 MHz</mem_clock>
//                </default_applications_clocks>
//                <max_clocks>
//                        <graphics_clock>758 MHz</graphics_clock>
//                        <sm_clock>758 MHz</sm_clock>
//                        <mem_clock>2600 MHz</mem_clock>
//                        <video_clock>540 MHz</video_clock>
//                </max_clocks>
//                <max_customer_boost_clocks>
//                        <graphics_clock>N/A</graphics_clock>
//                </max_customer_boost_clocks>
//                <clock_policy>
//                        <auto_boost>N/A</auto_boost>
//                        <auto_boost_default>N/A</auto_boost_default>
//                </clock_policy>
//                <supported_clocks>
//                        <supported_mem_clock>
//                                <value>2600 MHz</value>
//                                <supported_graphics_clock>758 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>705 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>666 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>640 MHz</supported_graphics_clock>
//                                <supported_graphics_clock>614 MHz</supported_graphics_clock>
//                        </supported_mem_clock>
//                        <supported_mem_clock>
//                                <value>324 MHz</value>
//                                <supported_graphics_clock>324 MHz</supported_graphics_clock>
//                        </supported_mem_clock>
//                </supported_clocks>
//                <processes>
//                </processes>
//                <accounted_processes>
//                </accounted_processes>
//        </gpu>
//
//</nvidia_smi_log>


}
