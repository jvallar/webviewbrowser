function generateTable(d_str, t_str) {

  str ='</table>';
  str += '<tr>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Date</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Time</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Latitude</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Longitude</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Timezone</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Phase Name</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Percent of full</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Age</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Rise Time</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;">Rise Location</td>';
  str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-right:0px;">Etc</td>';
  str += '</tr>';

  do {
    var jd = getSS('jd');
    var d_a = new Array(), t_a = new Array();
    if (d_str) {
      d_a = strDt2(d_str, getSS('dt_format'), jd);
    }
    if (t_str) {
      t_a = strT(t_str, getSS('clock'));
    }
    if (!d_a || !t_a) {
      return false;
    }

    if (OctVoid(d_a['Y'], d_a['M'], d_a['D'])) {
      return false;
    }

    var h = t24h(t_a['h'], t_a['ampm']);
    var jd = update_JD(jd, d_a['Y'], d_a['M'], d_a['D'], h, t_a['m'], t_a['s']);

    var str = "", rise_azimuth = "", set_azimuth = "";
    var jd = getSS('jd');
    var jdate = JD_to_date(jd);
    dy = jdate['D'];
    var tz = getSS('tz');

    var TZ_dst = DST_adj2(getSS('tz'), getSS('sys_tz'), getPS('tz'), jd, getSS('apply_dst'));
    var dt_format = getSS('dt_format');
    var clock = getSS('clock');
    var Lg = dms_to_d(getSS('Lg_d'), getSS('Lg_m'), getSS('Lg_s'), getSS('Lg_dir'));
    var Lt = dms_to_d(getSS('Lt_d'), getSS('Lt_m'), getSS('Lt_s'), getSS('Lt_dir'));
    var moon = getPhase(jd, TZ_dst);

    var rs = {rise: LocMsgAlert, set: LocMsgAlert};
    var H = new Object();
    var azimuth = LocMsgAlert, altitude = LocMsgAlert, az_dec = "", alt_dec = "";
    if (
     LtCheck(getSS('Lt_d'), getSS('Lt_m'), getSS('Lt_s'))
     &&
     LgCheck(getSS('Lg_d'), getSS('Lg_m'), getSS('Lg_s'))
     &&
     is_number(getPS('tz'))
     ) {
      rs = getRiseSet(jd, TZ_dst, Lg, Lt, getSS('clock')); // rem: dt is used to get mjd (just passes in generic y m d h)
      // rise pos
      if (rs.rise_dec) {
        var tmpDt = x_to_xms(rs.rise_dec);
        var tmpJD = date_to_JD(jdate['Y'], jdate['M'], jdate['D'], tmpDt['x'], tmpDt['m'], tmpDt['s']);
        var tmp_e = Ephemeris(tmpJD, TZ_dst);
        var rise_H = Horizon(tmp_e.MoonRa, tmp_e.MoonDec, tmpJD, TZ_dst, Lg, Lt);
        rise_azimuth = " at " + rnd(rise_H.az, 0) + "&deg";
      }
      if (rs.set_dec) {
        var tmpDt = x_to_xms(rs.set_dec);
        var tmpJD = date_to_JD(jdate['Y'], jdate['M'], jdate['D'], tmpDt['x'], tmpDt['m'], tmpDt['s']);
        var tmp_e = Ephemeris(tmpJD, TZ_dst);
        var set_H = Horizon(tmp_e.MoonRa, tmp_e.MoonDec, tmpJD, TZ_dst, Lg, Lt);
        set_azimuth = " at " + rnd(set_H.az, 0) + "&deg;";
      }
      H = Horizon(e.MoonRa, e.MoonDec, jd, TZ_dst, Lg, Lt);
      azimuth = dStr(H.az);
      az_dec = rnd(H.az, 4);
      altitude = dStr(H.alt);
      alt_dec = rnd(H.alt, 4);
    }
    var target = dtStr2(jd, dt_format) + ' ' + tStr2(jd, clock, 1);
    var phase_name = aPN[phN(moon.D)];
    var percent_of_full = rnd(moon.phase, 0);
    var age = rnd(moon.age, 0) + '% (' + age.d + ' days ' + age.h + ' hrs ' + pad2(age.m) + ' mins)';
    var rise = rs.rise + rise_azimuth;
    var set = rs.set + set_azimuth;
    str += '<tr>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + target + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + phase_name + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + percent_of_full + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + age + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + azimuth + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + altitude + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + rise + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + set + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + target + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;">' + target + '</td>';
    str += '<td style="border:1px solid #5A5A5B;border-left:0px;border-top:0px;border-right:0px;">' + target + '</td>';
    str += '</tr>';
  } while (dy < jdate['D']);
str +='</table>';
}