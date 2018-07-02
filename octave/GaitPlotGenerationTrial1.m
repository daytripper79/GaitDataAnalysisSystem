clear

printf ("Octave is working... \n");

delete(findall(0, 'Type', 'figure', 'FileName', []));
set(0,'DefaultAxesFontSize',14)

inputFile = argv(){1};

makFoot = argv(){2};

makLeft = 0;
if strcmp(makFoot,'L') == 1 
   makLeft = 1;
end 

% Retrievement of CVS files
calculatedFile = strcat(inputFile,".CSV.CALCULATED_DATA.csv");
calculatedData=csvread(calculatedFile);
gaitFile =  strcat(inputFile,".CSV.GAIT_DATA_MAK.csv");
gaitData=csvread(gaitFile);
pressureFile = strcat(inputFile,".CSV.PRESSURE_DATA_MAK.csv");
pressureData=csvread(pressureFile);

% CALCULATED Data columns
calculatedDataTime = calculatedData(2:end, 1);
calculatedDataPositionAngle = calculatedData(2:end, 2);
calculatedDataEulerX = calculatedData(2:end, 3);
calculatedDataEulerY = calculatedData(2:end, 4);
calculatedDataEulerZ = calculatedData(2:end, 5);
calculatedDataHallux = calculatedData(2:end, 6);
calculatedDataToes = calculatedData(2:end, 7);
calculatedDataMetaExt = calculatedData(2:end, 8);
calculatedDataMetaInterm = calculatedData(2:end, 9);
calculatedDataMetaInt = calculatedData(2:end, 10);
calculatedDataArch = calculatedData(2:end, 11);
calculatedDataHeelExt = calculatedData(2:end, 12);
calculatedDataHeelInt = calculatedData(2:end, 13);
calculatedDataTread = calculatedData(2:end, 14);
calculatedDataStep = calculatedData(2:end, 15);
calculatedDataCopX = calculatedData(2:end, 16);
calculatedDataCopY = calculatedData(2:end, 17);
calculatedDataRelativeCopX = calculatedData(2:end, 18);
calculatedDataRelativeCopY = calculatedData(2:end, 19);
calculatedDataMeanRelativeCopX = calculatedData(2:end, 20);
calculatedDataMeanRelativeCopY = calculatedData(2:end, 21);

% GAIT Data columns
gaitDataStep = gaitData(2:end, 1);
[free gaitDataPhase free free free free free free] = textread(gaitFile, '%d %s %d %d %d %d %d %d', 'delimiter', ',');
gaitDataInitialTime = gaitData(2:end, 3);
gaitDataFinalTime = gaitData(2:end, 4);
gaitDataTotalTime = gaitData(2:end, 5);
gaitDataStepTime = gaitData(2:end, 6);
gaitDataPercentTime = gaitData(2:end, 7);
gaitDataTotalGait = gaitData(2:end, 8);

% PRESSURE Data columns
pressureDataStep = pressureData(2:(end), 1);
pressureDataHalluxMax = pressureData(2:(end), 2);
pressureDataHalluxTotal = pressureData(2:(end), 3);
pressureDataHalluxRelative = pressureData(2:(end), 4);

pressureDataToesMax = pressureData(2:(end), 5);
pressureDataToesTotal = pressureData(2:(end), 6);
pressureDataToesRelative = pressureData(2:(end), 7);

pressureDataMetaExtMax = pressureData(2:(end), 8);
pressureDataMetaExtTotal = pressureData(2:(end), 9);
pressureDataMetaExtRelative = pressureData(2:(end), 10);

pressureDataMetaIntermMax = pressureData(2:(end), 11);
pressureDataMetaIntermTotal = pressureData(2:(end), 12);
pressureDataMetaIntermRelative = pressureData(2:(end), 13);

pressureDataMetaIntMax = pressureData(2:(end), 14);
pressureDataMetaIntTotal = pressureData(2:(end), 15);
pressureDataMetaIntRelative = pressureData(2:(end), 16);

pressureDataArchMax = pressureData(2:(end), 17);
pressureDataArchTotal = pressureData(2:(end), 18);
pressureDataArchRelative = pressureData(2:(end), 19);

pressureDataHeelIntMax = pressureData(2:(end), 20);
pressureDataHeelIntTotal = pressureData(2:(end), 21);
pressureDataHeelIntRelative = pressureData(2:(end), 22);

pressureDataHeelExtMax = pressureData(2:(end), 23);
pressureDataHeelExtTotal = pressureData(2:(end), 24);
pressureDataHeelExtRelative = pressureData(2:(end), 25);
                      

% Set NaN to 0 values as they dont be represented in plots
calculatedDataCopX(calculatedDataCopX==0) = nan;
calculatedDataCopY(calculatedDataCopY==0) = nan;
calculatedDataRelativeCopX(calculatedDataRelativeCopX==0) = nan;
calculatedDataRelativeCopY(calculatedDataRelativeCopY==0) = nan;
calculatedDataMeanRelativeCopX(calculatedDataMeanRelativeCopX==0) = nan;
calculatedDataMeanRelativeCopY(calculatedDataMeanRelativeCopY==0) = nan;

% CONSTANTS
insoleX =  max(calculatedDataCopX) + 15;
insoleY =  max(calculatedDataCopY) + 28;
footPosX = {'ext.' ; '' ; 'int.'};
footPosY = {'' ;'hindfoot' ; '' ;'middlefoot'; ''; 'forefoot'};

################################################################################

%indexFirstStep = 1;
%indexLastStep = length(gaitDataStep);

indexFirstStep = 5;
indexLastStep = 12;

################################################################################

firstStep = gaitDataStep(indexFirstStep);
lastStep  = gaitDataStep(indexLastStep);

initialTime = gaitDataInitialTime(indexFirstStep);
finalTime   = gaitDataFinalTime(indexLastStep);
initialIndex = find(calculatedDataTime == initialTime, 1, 'first');
finalIndex =   find(calculatedDataTime == finalTime, 1, 'first');

% PLOT POSITION ANGLE
fig = figure;
set(fig,'Resize', 'off') 
hold on
title('Angular position of knee')
ylabel("Degrees");
xlabel("Time(s)"); 
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
plot(calculatedDataTime, calculatedDataPositionAngle,'LineWidth',2,'color',[0.01 0.56 0.99]);
line(xlim(), [max(calculatedDataPositionAngle(initialIndex:finalIndex)) max(calculatedDataPositionAngle(initialIndex:finalIndex))], "linestyle", "--", "color", [0.85 0.078 0.23]);
line(xlim(), [min(calculatedDataPositionAngle(initialIndex:finalIndex)) min(calculatedDataPositionAngle(initialIndex:finalIndex))], "linestyle", "--", "color", [0.85 0.078 0.23]);
for i = indexFirstStep:2:indexLastStep
    line([gaitDataInitialTime(i) gaitDataInitialTime(i)], [ylim()], "linestyle", "--", "color", [0.41 0.41 0.41]);
end
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
set(gca,'ActivePositionProperty', 'position')
hold off
hgsave(gcf,strcat(inputFile,"_position.ofig"))
print(gcf,strcat(inputFile,"_position.png"))

% PLOT Euler X
figure;
hold on
title('Lateral displacement of hip')
plot(calculatedDataTime, calculatedDataEulerX,'LineWidth',2, 'color', [0.99 0.56 0.19])
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
ylabel("Degrees");
xlabel("Time(s)"); 
ylim([170 190])
line(xlim(), [max(calculatedDataEulerX(initialIndex:finalIndex)) max(calculatedDataEulerX(initialIndex:finalIndex))], "linestyle", "--", "color", [0.85 0.078 0.23]);
line(xlim(), [min(calculatedDataEulerX(initialIndex:finalIndex)) min(calculatedDataEulerX(initialIndex:finalIndex))], "linestyle", "--", "color", [0.85 0.078 0.23]);
for i = indexFirstStep:2:indexLastStep
    line([gaitDataInitialTime(i) gaitDataInitialTime(i)], [ylim()], "linestyle", "--", "color", [0.41 0.41 0.41]);
end
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
set(gca,'ActivePositionProperty', 'position')
hgsave(gcf,strcat(inputFile,"_eulerx.ofig"))
print(gcf,strcat(inputFile,"_eulerx.png"))
hold off

% PLOT Euler Y
fig = figure;
set(fig,'Resize', 'off') 
hold on
title('Forward-Backward Displacement of hip')
plot(calculatedDataTime, calculatedDataEulerY,'LineWidth',2, 'color',[0 0.5 0])
xlim([min(calculatedDataTime) max(calculatedDataTime)])
ylabel("Degrees");
xlabel("Time(s)"); 
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
line(xlim(), [max(calculatedDataEulerY(initialIndex:finalIndex)) max(calculatedDataEulerY(initialIndex:finalIndex))], "linestyle", "--", 'color',[0.85 0.078 0.23]);
line(xlim(), [min(calculatedDataEulerY(initialIndex:finalIndex)) min(calculatedDataEulerY(initialIndex:finalIndex))], "linestyle", "--", 'color',[0.85 0.078 0.23]);
for i = indexFirstStep:2:indexLastStep
    line([gaitDataInitialTime(i) gaitDataInitialTime(i)], [ylim()], "linestyle", "--", "color", [0.41 0.41 0.41]);
end
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
set(gca,'ActivePositionProperty', 'position')
hgsave(gcf,strcat(inputFile,"_eulery.ofig"))
print(gcf,strcat(inputFile,"_eulery.png"))
hold off

% PLOT INSOLE
fig = figure;
set(fig,'Resize', 'off') 
hold on
plot(calculatedDataTime, calculatedDataHallux, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataToes, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataMetaExt, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataMetaInterm, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataMetaInt, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataArch, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataHeelExt, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataHeelInt, 'LineWidth',2,'color', [0.93 0.5 0.5])
for i = indexFirstStep:2:indexLastStep
    line([gaitDataInitialTime(i) gaitDataInitialTime(i)], [ylim()], "linestyle", "--", "color", [0.41 0.41 0.41]);
end
title('Insole pressures by time in MAK')
ylabel("Insole sensors (millibar)");
xlabel("Time(s)"); 
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
set(gca,'ActivePositionProperty', 'position')
hgsave(gcf,strcat(inputFile,"_gait.ofig"))
legend({'Hallux', 'Toes', 'Met Ext', 'Met Interm','Met Int', 'Arch', 'Heel Ext', 'Heel Int'},'FontSize',10,'Location','southoutside', 'Orientation','horizontal')
print(gcf,strcat(inputFile,"_gait.png"))
hold off

% PLOT TOTAL Pressures in sensors by step
fig = figure;
set(fig,'Resize', 'off') 
hold on
bar(pressureDataStep, [pressureDataHalluxTotal pressureDataToesTotal pressureDataMetaExtTotal pressureDataMetaIntermTotal pressureDataMetaIntTotal pressureDataArchTotal pressureDataHeelExtTotal pressureDataHeelIntTotal] )
title('Total pressure in sensors by step')
xlabel("Steps"); 
ylabel("Sensor's Total pressure(mb)"); 
set(gca,'xtick',[])
xlim([firstStep lastStep])
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hgsave(gcf,strcat(inputFile,"_pressuretotal.ofig"))
legend({'Hallux', 'Toes', 'Met Ext', 'Met Interm','Met Int', 'Arch', 'Heel Ext', 'Heel Int'},'Location','northeastoutside')
print(gcf,strcat(inputFile,"_pressuretotal.png"))
hold off


% PLOT MAX Pressures in sensors by step
fig = figure;
set(fig,'Resize', 'off') 
hold on
bar(pressureDataStep, [pressureDataHalluxMax pressureDataToesMax pressureDataMetaExtMax pressureDataMetaIntermMax pressureDataMetaIntMax pressureDataArchMax pressureDataHeelExtMax pressureDataHeelIntMax] )
title('Max pressure in sensors by step')
xlabel("Steps"); 
ylabel("Sensor's Max pressure (millibar)"); 
set(gca,'xtick',[])
xlim([firstStep lastStep])
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hgsave(gcf,strcat(inputFile,"_pressuremax.png"))
legend({'Hallux', 'Toes', 'Met Ext', 'Met Interm','Met Int', 'Arch', 'Heel Ext', 'Heel Int'},'Location','northeastoutside')
print(gcf,strcat(inputFile,"_pressuremax.png"))
hold off

% PLOT RELATIVE Pressures in sensors by step
fig = figure;
set(fig,'Resize', 'off') 
hold on
bar(pressureDataStep, [pressureDataHalluxRelative pressureDataToesRelative pressureDataMetaExtRelative pressureDataMetaIntermRelative pressureDataMetaIntRelative pressureDataArchRelative pressureDataHeelExtRelative pressureDataHeelIntRelative] )
title('Relative pressure in sensors by step')
xlabel("Steps"); 
ylabel("Sensor's relative pressure (%)"); 
set(gca,'xtick',[])
xlim([firstStep lastStep])
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hgsave(gcf,strcat(inputFile,"_pressurerel.ofig"))
legend({'Hallux', 'Toes', 'Met Ext', 'Met Interm','Met Int', 'Arch', 'Heel Ext', 'Heel Int'},'Location','northeastoutside')
print(gcf,strcat(inputFile,"_pressurerel.png"))
hold off

% PLOT COP X (%)
fig = figure;
set(fig,'Resize', 'off') 
ax1 = subplot(2,1,1);
hold on
plot(calculatedDataTime, calculatedDataCopX, 'LineWidth',2,'color',[0.41 0.35 0.80]);
title('Cop X position by Time')
xlabel("Time(s)"); 
ylabel("CoP X position"); 
xlim([initialTime finalTime])
for i = indexFirstStep:2:indexLastStep
    line([gaitDataInitialTime(i) gaitDataInitialTime(i)], [ylim()], "linestyle", "--", "color", [0.41 0.41 0.41]);
end
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
ylim([0 insoleX])
range = insoleX/2;
set(gca, 'ytick', [0 range insoleX] ,'yticklabel',footPosX)
grid on
hold off

% PLOT COP Y
ax2 = subplot(2,1,2);
hold on
plot(calculatedDataTime, calculatedDataCopY, 'LineWidth',2,'color',[0.99 0.83 0]);
title('Cop Y position by Time')
xlabel("Time(s)"); 
ylabel("CoP Y position"); 
xlim([initialTime finalTime])
for i = indexFirstStep:2:indexLastStep
    line([gaitDataInitialTime(i) gaitDataInitialTime(i)], [ylim()], "linestyle", "--", "color", [0.41 0.41 0.41]);
end
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
ylim([0 insoleY])
range = insoleY/6;
set(gca, 'ytick', 0:range:insoleY ,'yticklabel',footPosY)
grid on
hold off
hgsave(gcf,strcat(inputFile,"_cop",".ofig"))
print(gcf,strcat(inputFile,"_cop",".png"))

for i = calculatedDataStep(initialIndex):2:calculatedDataStep(finalIndex)
 
  initialIndex = find(calculatedDataStep == i, 1, 'first');
  finalIndex =   find(calculatedDataStep == i, 1, 'last');
  interval =  calculatedDataTime(finalIndex) - calculatedDataTime(initialIndex);
  rangeInterval = interval/2;
  a = sprintf('%d',0);
  b = sprintf('%d',rangeInterval);
  c = sprintf('%d',interval);
  labels = [ a ; b ; c];
 
  figure
  % PLOT Isolated Step CoP
  hold on
  str = sprintf('Cop trayectory %d', i);
  title(str);
  axis equal
  xlabel("CoP X")   
  ylabel("CoP Y") 
 
  if makLeft == 0
       set(gca, 'XDir', 'reverse')
  end 
  
  x = calculatedDataCopX(initialIndex:finalIndex);
  xlim([0 insoleX])
  range = insoleX/2;
  set(gca, 'xtick', [0 range insoleX] ,'xticklabel',footPosX)
  y = calculatedDataCopY(initialIndex:finalIndex);
  ylim([0 insoleY])
  range = insoleY/6;
  set(gca, 'ytick', 0:range:insoleY ,'yticklabel',footPosY)
  z = calculatedDataTime(initialIndex:finalIndex);  
  % Reshape and replicate data
  surf([x(:) x(:)], [y(:) y(:)], [z(:) z(:)], 'FaceColor', 'none', 'EdgeColor', 'interp', 'LineWidth', 3);            
  % Default 2-D view 
  view(2)
  cbh = colorbar;
  set(cbh,'YTick', [], 'YLabel', 'Time progression') 
  grid on
  hold off
  str = sprintf('%d', i);
  hgsave(gcf,strcat(inputFile,"_stepcop-", str, ".ofig"))
  print(gcf,strcat(inputFile,"_stepcop-", str, ".png"))
  
  % PLOT Isolated Step 
  figure
  hold on
  str = sprintf('Insole sensors progression by time for Step %d', i);
  title(str)
  xlim([calculatedDataTime(initialIndex) calculatedDataTime(finalIndex)])
  set(gca, 'xtick', [calculatedDataTime(initialIndex):rangeInterval:calculatedDataTime(finalIndex)] ,'xticklabel', labels)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataHallux(initialIndex:finalIndex), 'LineWidth', 2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataToes(initialIndex:finalIndex), 'LineWidth', 2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataMetaExt(initialIndex:finalIndex), 'LineWidth', 2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataMetaInterm(initialIndex:finalIndex), 'LineWidth', 2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataMetaInt(initialIndex:finalIndex), 'LineWidth', 2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataArch(initialIndex:finalIndex), 'LineWidth', 2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataHeelExt(initialIndex:finalIndex), 'LineWidth', 2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataHeelInt(initialIndex:finalIndex), 'LineWidth', 2,'color', [0.93 0.5 0.5])
  ylabel("Insole sensors (millibar)");
  xlabel("Time(s)"); 
  grid on
  hold off
  str = sprintf('%d', i);
  hgsave(gcf,strcat(inputFile,"_step-", str, ".ofig"))
  legend({'Hallux', 'Toes', 'Met Ext', 'Met Interm','Met Int', 'Arch', 'Heel Ext', 'Heel Int'},'FontSize',10,'Location','southoutside', 'Orientation','horizontal')
  print(gcf,strcat(inputFile,"_step-", str, ".png"))

end


